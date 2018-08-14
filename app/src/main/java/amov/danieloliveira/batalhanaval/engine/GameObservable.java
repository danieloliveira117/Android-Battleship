package amov.danieloliveira.batalhanaval.engine;

import java.util.Observable;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.enums.PositionType;
import amov.danieloliveira.batalhanaval.engine.model.Position;
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
        notifyObservers(user);
    }

    public void addNewAttempt(PlayerType playerType, Position position) {
        gameData.addNewAttempt(playerType, position);

        setChanged();
        notifyObservers();
    }

    public PositionType getPositionType(PlayerType player, Position position) {
        return gameData.getPositionType(player, position);
    }

    public void placeShip(PlayerType player, Position position, Integer tag) {
        gameData.placeShip(player, position, tag);

        setChanged();
        notifyObservers();
    }
}
