package amov.danieloliveira.batalhanaval.engine;

import java.util.List;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.enums.PositionType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidShipNumberException;
import amov.danieloliveira.batalhanaval.engine.model.Player;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.Ship;
import amov.danieloliveira.batalhanaval.engine.model.User;

class GameModel {
    private Player player1, player2;
    private GameMode gameMode;

    public GameModel() {
        player1 = new Player(true);
        player2 = new Player(false);
    }

    public void setMode(GameMode mode) {
        gameMode = mode;
    }

    public void updatePlayerData(PlayerType player, User user) {
        switch (player) {
            case PLAYER:
                player1.setUser(user);
                break;
            case ADVERSARY:
                player2.setUser(user);
                player2.setHuman(true);
                break;
        }
    }

    public Ship[] getPlayerShips(PlayerType player) {
        return getPlayer(player).getBoard().getShipList();
    }

    public boolean allShipsPlaced() {
        return getPlayer(PlayerType.PLAYER).allShipsPlaced() && getPlayer(PlayerType.ADVERSARY).allShipsPlaced();
    }

    public void setShipsPlaced(PlayerType player) {
        getPlayer(player).setShipsPlaced(true);
    }

    Player getPlayer(PlayerType player) {
        switch (player) {
            case PLAYER:
                return player1;
            default:
                return player2;
        }
    }

    public boolean addNewAttempt(PlayerType player, Position position) {
        return getPlayer(player).addNewAttempt(position);
    }

    public PositionType getPositionType(PlayerType player, Position position) {
        return getPlayer(player).getPositionType(position);
    }

    public Ship getPlayerShip(PlayerType player, Integer ship) throws InvalidShipNumberException {
        return getPlayer(player).getShipByID(ship);
    }

    public Ship getPlayerShip(PlayerType player, Position position) {
        return getPlayer(player).getShipByPosition(position);
    }

    public PositionType getPositionValidity(PlayerType player, Position position) {
        return getPlayer(player).getPositionValidity(position);
    }

    public List<Position> getShipPositions(PlayerType player, Position position) {
        return getPlayer(player).getShipPositions(position);
    }
}
