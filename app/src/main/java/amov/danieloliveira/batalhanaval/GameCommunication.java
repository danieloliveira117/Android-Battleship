package amov.danieloliveira.batalhanaval;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.activities.GameActivity;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.JsonMessage;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.MsgType;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Board;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.User;
import amov.danieloliveira.batalhanaval.engine.model.UserBase64;
import amov.danieloliveira.batalhanaval.activities.GameStartActivity;

import static amov.danieloliveira.batalhanaval.Consts.CLIENT;
import static amov.danieloliveira.batalhanaval.Consts.PORT;
import static amov.danieloliveira.batalhanaval.Consts.SERVER;

public class GameCommunication implements Observer {
    private static final String TAG = "GameCommunication";

    private PlayerType playerType;
    private PlayerType opponentType;
    private GameObservable gameObs;
    private Activity activity;
    private Handler procMsg;
    private ProgressDialog pd = null;
    private ServerSocket serverSocket = null;
    private Socket socketGame = null;
    private BufferedReader input;
    private PrintWriter output;
    private int mode;

    GameCommunication(GameObservable gameObs, Activity activity, int mode) {
        this.gameObs = gameObs;
        this.activity = activity;
        this.procMsg = new Handler();
        this.mode = mode;

        if (mode == CLIENT) {
            playerType = PlayerType.ADVERSARY;
            opponentType = PlayerType.PLAYER;
        } else {
            playerType = PlayerType.PLAYER;
            opponentType = PlayerType.ADVERSARY;
        }

        gameObs.addObserver(this);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void startCommunication() {
        switch (mode) {
            case SERVER:
                server();
                break;
            case CLIENT:
                clientDlg();
                break;
        }
    }

    public void endCommunication() {
        try {
            gameObs.deleteObserver(this);
            commThread.interrupt();

            if (socketGame != null) {
                socketGame.close();
            }

            if (output != null) {
                output.close();
            }

            if (input != null) {
                input.close();
            }
        } catch (Exception ignored) {
        }

        input = null;
        output = null;
        socketGame = null;
    }

    private <T> void SendMessage(T obj, MsgType type) {
        Gson gson = new Gson();

        JsonMessage<T> jsonMessage = new JsonMessage<>(obj, type);
        final String json = gson.toJson(jsonMessage, JsonMessage.class);

        new Thread(new Runnable() {
            @Override
            public void run() {
                output.println(json);
                output.flush();
            }
        }).start();
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
                    String read = input.readLine();
                    HandleMessage(read);
                }
            } catch (Exception e) {
                //Log.e(TAG, "HandleMessage", e);
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

            Log.d(TAG, "Communication thread has been interrupted.");
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
                        final Type type = new TypeToken<JsonMessage<UserBase64>>() {
                        }.getType();
                        final JsonMessage jsonMessage = gson.fromJson(mJson, type);

                        User player;
                        User adversary;

                        if (mode == CLIENT) {
                            player = ((UserBase64) jsonMessage.getObject()).toUser();
                            adversary = Utils.getUser(activity);
                        } else {
                            adversary = ((UserBase64) jsonMessage.getObject()).toUser();
                            player = Utils.getUser(activity);
                        }

                        // Set adversary
                        gameObs.setAdversaryUser(adversary);

                        // Start Game
                        gameObs.startGame(GameMode.vsPLAYER, player, mode == CLIENT);

                        if (mode == SERVER) {
                            gameObs.sendStartingPlayer();
                        }
                    }
                    break;
                    case CONFIRM_PLACEMENT: {
                        final Type type = new TypeToken<JsonMessage<Board>>() {
                        }.getType();
                        final JsonMessage jsonMessage = gson.fromJson(mJson, type);

                        gameObs.confirmPlacementRemote(opponentType, (Board) jsonMessage.getObject());
                    }
                    break;
                    case STARTING_PLAYER: {
                        final Type type = new TypeToken<JsonMessage<PlayerType>>() {
                        }.getType();
                        final JsonMessage jsonMessage = gson.fromJson(mJson, type);

                        gameObs.setStartingPlayerRemote((PlayerType) jsonMessage.getObject());
                    }
                    break;
                    case CLICK_POSITION: {
                        final Type type = new TypeToken<JsonMessage<Position>>() {
                        }.getType();
                        final JsonMessage jsonMessage = gson.fromJson(mJson, type);

                        gameObs.clickPositionRemote(opponentType, (Position) jsonMessage.getObject());
                    }
                    break;
                }

                Log.d(TAG, "Received " + tempMessage.getType());
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

    @SuppressLint("SetTextI18n")
    private void clientDlg() {
        final EditText edtIP = new EditText(activity);
        edtIP.setText("192.168.1.116"); // emulator's default ip 10.0.2.2

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
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "Connecting to the server  " + strIP);
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            pd.show();
                        }
                    });

                    socketGame = new Socket(strIP, Port);
                } catch (Exception e) {
                    socketGame = null;
                }

                pd.dismiss();

                if (socketGame == null) {
                    procMsg.post(new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                            Toast.makeText(activity.getApplicationContext(), R.string.could_not_connect_to_server, Toast.LENGTH_SHORT).show();
                        }
                    });

                    return;
                }

                commThread.start();
            }
        });

        pd = new ProgressDialog(activity);
        pd.setMessage(activity.getString(R.string.clientdlg_connecting) + "\n(IP: " + strIP + ":" + Port + ")");
        pd.setTitle(R.string.clientdlg_msg);

        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                activity.finish();
                t.interrupt();
                socketGame = null;
            }
        });

        t.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == MsgType.CONFIRM_PLACEMENT) {
            if (gameObs.validPlacement(playerType)) {
                SendMessage(gameObs.getPlayerBoard(playerType), MsgType.CONFIRM_PLACEMENT);
            }
        } else if (arg == MsgType.STARTING_PLAYER) {
            SendMessage(gameObs.getCurrentPlayer(), MsgType.STARTING_PLAYER);
        } else if (arg instanceof Position) {
            SendMessage(arg, MsgType.CLICK_POSITION);
        }
    }
}
