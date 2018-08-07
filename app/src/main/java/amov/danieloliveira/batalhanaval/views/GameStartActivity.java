package amov.danieloliveira.batalhanaval.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;

import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.JsonMessage;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.MsgType;
import amov.danieloliveira.batalhanaval.engine.model.User;
import amov.danieloliveira.batalhanaval.engine.model.UserBase64;

import static amov.danieloliveira.batalhanaval.Consts.PORT;
import static amov.danieloliveira.batalhanaval.Consts.SERVER;

public class GameStartActivity extends AppCompatActivity {
    private static final String TAG = "GameStartActivity";

    int players = 1;
    int mode = SERVER;
    ServerSocket serverSocket = null;
    Socket socketGame = null;
    BufferedReader input;
    PrintWriter output;
    Handler procMsg = null;
    ProgressDialog pd = null;

    Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socketGame.getInputStream()));
                output = new PrintWriter(socketGame.getOutputStream());

                // Convert image to base64 string
                UserBase64 userBase64 = new UserBase64(Utils.getUser(GameStartActivity.this));

                // Send User Data
                SendMessage(userBase64, MsgType.USER64);

                while (!Thread.currentThread().isInterrupted()) {
                    HandleMessage(input.readLine());
                }
            } catch (Exception e) {
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        finish();

                        Toast.makeText(getApplicationContext(),
                                R.string.game_finished, Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        }
    });

    public void HandleMessage(String input) {
        JsonParser parser = new JsonParser();
        final JsonElement mJson = parser.parse(input);

        final Gson gson = new Gson();
        final JsonMessage tempMessage = gson.fromJson(mJson, JsonMessage.class);

        procMsg.post(new Runnable() {
            @Override
            public void run() {
                GameObservable gameObs = Utils.getObservable(GameStartActivity.this);

                switch (tempMessage.getType()) {
                    case USER64: {
                        final Type type = new TypeToken<JsonMessage<UserBase64>>() {}.getType();
                        final JsonMessage jsonMessage = gson.fromJson(mJson, type);

                        User adversary = ((UserBase64) jsonMessage.getObject()).toUser();

                        // Update Views
                        CircularImageView civ_adversary_avatar = findViewById(R.id.civ_adversary_avatar);
                        AppCompatTextView tv_adversary_username = findViewById(R.id.tv_adversary_username);

                        civ_adversary_avatar.setImageBitmap(adversary.getImage());
                        tv_adversary_username.setText(adversary.getUsername());

                        // Start Game
                        gameObs.setAdversary(adversary);
                        gameObs.startGame(GameMode.vsPLAYER, Utils.getUser(GameStartActivity.this));
                    }
                }
            }
        });
    }

    public <T> void SendMessage(T obj, MsgType type) {
        Gson gson = new Gson();

        JsonMessage<T> jsonMessage = new JsonMessage<>(obj, type);
        String json = gson.toJson(jsonMessage, JsonMessage.class);

        output.println(json);
        output.flush();
    }

    void server() {
        String ip = Utils.getLocalIpAddress();

        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.serverdlg_msg) + "\n(IP: " + ip + ")");
        pd.setTitle(R.string.serverdlg_title);

        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException ignored) {
                    }

                    serverSocket = null;
                }
            }
        });

        pd.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    socketGame = serverSocket.accept();
                    serverSocket.close();
                    serverSocket = null;
                    commThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    socketGame = null;
                }

                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (socketGame == null)
                            finish();
                    }
                });
            }
        });

        t.start();
    }

    void clientDlg() {
        final EditText edtIP = new EditText(this);
        edtIP.setText("10.0.2.2"); // emulator's default ip

        AlertDialog ad = new AlertDialog.Builder(this).setTitle(getString(R.string.clientdlg_msg))
                .setMessage(getString(R.string.serverip)).setView(edtIP)
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        client(edtIP.getText().toString(), PORT); // to test with emulators: PORT_AUX);
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                }).create();
        ad.show();
    }

    void client(final String strIP, final int Port) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "Connecting to the server  " + strIP);
                    socketGame = new Socket(strIP, Port);
                } catch (Exception e) {
                    socketGame = null;
                }

                if (socketGame == null) {
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                    return;
                }

                commThread.start();
            }
        });

        t.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mode == SERVER)
            server();
        else  // CLIENT
            clientDlg();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            commThread.interrupt();
            if (socketGame != null)
                socketGame.close();

            if (output != null)
                output.close();

            if (input != null)
                input.close();
        } catch (Exception ignored) {
        }

        input = null;
        output = null;
        socketGame = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connMgr != null;

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this, R.string.error_netconn, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getIntExtra("mode", SERVER);
            players = intent.getIntExtra("players", 1);
        }

        procMsg = new Handler();
    }
}
