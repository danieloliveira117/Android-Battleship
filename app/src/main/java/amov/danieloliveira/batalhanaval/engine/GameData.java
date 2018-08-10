package amov.danieloliveira.batalhanaval.engine;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Ship;
import amov.danieloliveira.batalhanaval.engine.model.User;
import amov.danieloliveira.batalhanaval.engine.state.AwaitGameStart;
import amov.danieloliveira.batalhanaval.engine.state.IGameState;

public class GameData {
    private GameModel gameModel;
    private IGameState currentState;
    private Ship[] currentShip = {null, null};

    GameData() {
        this.gameModel = new GameModel();
        this.currentState = new AwaitGameStart(this);
    }

    /* States */
    public void startGame(GameMode mode, User user) {
        currentState = currentState.startGame(mode, user);
    }

    public void setAdversary(User user) {
        currentState = currentState.setAdversary(user);
    }

    /* Update Game Model */
    public void prepareGame(GameMode mode) {
        gameModel.setMode(mode);

        // TODO: 07/08/2018 anything else needed here?
    }

    public void updatePlayerData(PlayerType player, User user) {
        gameModel.updatePlayerData(player, user);
    }

    /* Gets */
    public Ship[] getPlayerShips(PlayerType player) {
        return gameModel.getPlayerShips(player);
    }

    public Ship getCurrentShip(PlayerType player) {
        switch (player) {
            case PLAYER:
                return currentShip[0];
            case ADVERSARY:
                return currentShip[1];
        }

        return null;
    }

    public void setCurrentShip(PlayerType player, Ship ship) {
        switch (player) {
            case PLAYER:
                currentShip[0] = ship;
            case ADVERSARY:
                currentShip[1] = ship;
        }
    }

    public boolean allShipsPlaced() {
        return gameModel.allShipsPlaced();
    }

    public void confirmShipPlacement(PlayerType player) {
        gameModel.confirmShipPlacement(player);
    }
}
