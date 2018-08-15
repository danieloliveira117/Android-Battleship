package amov.danieloliveira.batalhanaval;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;

import amov.danieloliveira.batalhanaval.activities.GameStartActivity;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class BattleshipApplication extends Application {
    private GameObservable obs;
    private GameCommunication gameCommunication;
    private User user;

    public BattleshipApplication() {
        this.obs = new GameObservable();
        this.gameCommunication = null;
    }

    public GameObservable getObservable() {
        return obs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameObservable getObs() {
        return obs;
    }

    public void setObs(GameObservable obs) {
        this.obs = obs;
    }

    public GameCommunication getGameCommunication() {
        return gameCommunication;
    }

    public GameCommunication newGameCommunication(GameStartActivity activity, int mode) {
        if (gameCommunication != null) {
            gameCommunication.endCommunication();
        }

        gameCommunication = new GameCommunication(activity, mode);

        return gameCommunication;
    }
}
