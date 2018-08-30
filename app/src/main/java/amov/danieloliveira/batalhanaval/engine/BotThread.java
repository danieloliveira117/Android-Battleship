package amov.danieloliveira.batalhanaval.engine;

import android.os.Handler;
import android.util.Log;

import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;

public class BotThread extends Thread {
    private static final String TAG = "BotThread";

    private final PlayerType type;
    private final GameObservable gameObs;
    private final Handler handler;

    private boolean terminateThread;

    public BotThread(GameObservable gameObs, PlayerType opponent) {
        this.gameObs = gameObs;
        this.handler = new Handler();
        this.terminateThread = false;
        this.type = opponent;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        while (!terminateThread) {
            if (gameObs.getCurrentPlayer() == type) {
                if (gameObs.isShipReposition(type)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            gameObs.confirmPlacement(type);
                        }
                    });

                    try {
                        Thread.sleep(900);
                    } catch (InterruptedException ignored) {
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            gameObs.playRandomly(type);
                        }
                    });
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }

        Log.d(TAG, "Thread has been interrupted.");
    }

    public void terminateThread() {
        this.terminateThread = true;
    }
}
