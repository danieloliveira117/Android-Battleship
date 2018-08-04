package amov.danieloliveira.batalhanaval.engine;

import java.util.Observable;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;

public class GameObservable extends Observable {
    private GameData gameData;

    public GameObservable() {
        this.gameData = new GameData();
    }

    /* --- SETS --- */
    public void startGame(GameMode mode) {
        gameData.startGame(mode);

        setChanged();
        notifyObservers();
    }
}
