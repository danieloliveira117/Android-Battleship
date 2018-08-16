package amov.danieloliveira.batalhanaval.engine;

import android.os.Handler;

import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;

public class BotThread extends Thread {
    private GameObservable gameObs;
    private Handler handler;

    public BotThread(Handler handler, GameObservable gameObs) {
        this.gameObs = gameObs;
        this.handler = handler;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            if(gameObs.getCurrentPlayer() == PlayerType.ADVERSARY) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameObs.playRandomly();
                    }
                });
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }
    }
}
