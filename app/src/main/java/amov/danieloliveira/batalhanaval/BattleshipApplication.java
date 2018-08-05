package amov.danieloliveira.batalhanaval;

import android.app.Application;

import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class BattleshipApplication extends Application {
    private GameObservable obs;
    private User user;

    public BattleshipApplication() {
        this.obs = new GameObservable();
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
}
