package amov.danieloliveira.batalhanaval.engine;

import android.os.Handler;
import android.util.Log;

import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;

public class BotThread extends Thread {
    private static final String TAG = "BotThread";

    private GameObservable gameObs;
    private Handler handler;
    private boolean terminateThread;

    public BotThread(GameObservable gameObs) {
        this.gameObs = gameObs;
        this.handler = new Handler();
        this.terminateThread = false;
    }

    @Override
    public void run() {
        while (!terminateThread) {
            if (gameObs.getCurrentPlayer() == PlayerType.ADVERSARY) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameObs.playRandomly();
                    }
                });
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
