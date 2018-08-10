package amov.danieloliveira.batalhanaval;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;

import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.JsonMessage;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.MsgType;
import amov.danieloliveira.batalhanaval.engine.model.User;
import amov.danieloliveira.batalhanaval.engine.model.UserBase64;
import amov.danieloliveira.batalhanaval.activities.GameStartActivity;

import static amov.danieloliveira.batalhanaval.Consts.CLIENT;
import static amov.danieloliveira.batalhanaval.Consts.PORT;
import static amov.danieloliveira.batalhanaval.Consts.SERVER;

public class GameCommunication {
    private static final String TAG = "GameCommunication";

    private GameStartActivity activity;
    private Handler procMsg;
    private ProgressDialog pd = null;
    private ServerSocket serverSocket = null;
    private Socket socketGame = null;
    private BufferedReader input;
    private PrintWriter output;

    public GameCommunication(GameStartActivity activity, Handler procMsg, int mode) {
        this.activity = activity;
        this.procMsg = procMsg;

        switch (mode) {
            case SERVER:
                server();
                break;
            case CLIENT:
                clientDlg();
                break;
        }
    }

    public <T> void SendMessage(T obj, MsgType type) {
        Gson gson = new Gson();

        JsonMessage<T> jsonMessage = new JsonMessage<>(obj, type);
        String json = gson.toJson(jsonMessage, JsonMessage.class);

        output.println(json);
        output.flush();
    }

    public void onPause() {
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

    private Thread commThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socketGame.getInputStream()));
                output = new PrintWriter(socketGame.getOutputStream());

                // Convert image to base64 string
                UserBase64 userBase64 = new UserBase64(Utils.getUser(activity));

                // Send User Data
                SendMessage(userBase64, MsgType.USER64);

                while (!Thread.currentThread().isInterrupted()) {
                    HandleMessage(input.readLine());
                }
            } catch (Exception e) {
                procMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();

                        Toast.makeText(activity.getApplicationContext(),
                                R.string.game_finished, Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        }
    });

    private void HandleMessage(String input) {
        final JsonParser parser = new JsonParser();
        final JsonElement mJson = parser.parse(input);

        final Gson gson = new Gson();
        final JsonMessage tempMessage = gson.fromJson(mJson, JsonMessage.class);

        procMsg.post(new Runnable() {
            @Override
            public void run() {
                GameObservable gameObs = Utils.getObservable(activity);

                switch (tempMessage.getType()) {
                    case USER64: {
                        final Type type = new TypeToken<JsonMessage<UserBase64>>() {}.getType();
                        final JsonMessage jsonMessage = gson.fromJson(mJson, type);

                        User adversary = ((UserBase64) jsonMessage.getObject()).toUser();

                        // Set adversary
                        gameObs.setAdversary(adversary);

                        // Start Game
                        gameObs.startGame(GameMode.vsPLAYER, Utils.getUser(activity));
                    }
                }
            }
        });
    }

    private void server() {
        String ip = Utils.getLocalIpAddress();

        pd = new ProgressDialog(activity);
        pd.setMessage(activity.getString(R.string.serverdlg_msg) + "\n(IP: " + ip + ")");
        pd.setTitle(R.string.serverdlg_title);

        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                activity.finish();
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
                        if (socketGame == null) {
                            activity.finish();
                        }
                    }
                });
            }
        });

        t.start();
    }

    private void clientDlg() {
        final EditText edtIP = new EditText(activity);
        edtIP.setText("10.0.2.2"); // emulator's default ip

        AlertDialog ad = new AlertDialog.Builder(activity).setTitle(activity.getString(R.string.clientdlg_msg))
                .setMessage(activity.getString(R.string.serverip)).setView(edtIP)
                .setPositiveButton(activity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        client(edtIP.getText().toString(), PORT); // to test with emulators: PORT_AUX);
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        activity.finish();
                    }
                }).create();

        ad.show();
    }

    private void client(final String strIP, final int Port) {
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
                            activity.finish();
                        }
                    });
                    return;
                }

                commThread.start();
            }
        });

        t.start();
    }
}
