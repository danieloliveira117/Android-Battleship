package amov.danieloliveira.batalhanaval.engine;

import java.util.Observable;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class GameObservable extends Observable {
    private GameData gameData;

    public GameObservable() {
        this.gameData = new GameData();
    }

    /* --- SETS --- */
    public void startGame(GameMode mode, User user) {
        gameData.startGame(mode, user);

        setChanged();
        notifyObservers();
    }

    public void setAdversary(User user) {
        gameData.setAdversary(user);

        setChanged();
        notifyObservers();
    }
}
