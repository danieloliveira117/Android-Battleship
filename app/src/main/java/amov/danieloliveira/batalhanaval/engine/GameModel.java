package amov.danieloliveira.batalhanaval.engine;

import java.util.List;
import java.util.Random;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.enums.PositionType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidShipNumberException;
import amov.danieloliveira.batalhanaval.engine.model.Board;
import amov.danieloliveira.batalhanaval.engine.model.Player;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.Ship;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class GameModel {
    private Player player1, player2;
    private GameMode gameMode;
    private int numPlays;

    GameModel() {
        player1 = new Player();
        player2 = new Player();
        numPlays = 0;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setMode(GameMode mode) {
        gameMode = mode;
    }

    public void updatePlayerData(PlayerType player, User user) {
        getPlayer(player).setUser(user);
    }

    public boolean allShipsPlaced() {
        return getPlayer(PlayerType.PLAYER).allShipsPlaced() && getPlayer(PlayerType.ADVERSARY).allShipsPlaced();
    }

    public boolean allShipsPlaced(PlayerType player) {
        return getPlayer(player).allShipsPlaced();
    }

    public void setShipsPlaced(PlayerType player) {
        getPlayer(player).setShipsPlaced();
    }

    private Player getOpponent(PlayerType player) {
        if (player == PlayerType.PLAYER)
            return player2;
        else
            return player1;
    }

    public Player getPlayer(PlayerType type) {
        if (type == PlayerType.PLAYER)
            return player1;
        else
            return player2;
    }

    public void addNewAttempt(PlayerType player, Position position) {
        getOpponent(player).addNewAttempt(position);
    }

    public PositionType getPositionType(PlayerType player, Position position, boolean isOpponentView) {
        return getOpponent(player).getPositionType(position, isOpponentView);
    }

    public Ship getPlayerShip(PlayerType player, Integer ship) throws InvalidShipNumberException {
        return getPlayer(player).getShipByID(ship);
    }

    public Ship getPlayerShip(PlayerType player, Position position) {
        return getPlayer(player).getShipByPosition(position);
    }

    public PositionType getPositionValidity(PlayerType player, Position position, Ship currentShip) {
        return getPlayer(player).getPositionValidity(position, currentShip);
    }

    public List<Position> getShipPositions(PlayerType player, Position position) {
        return getPlayer(player).getShipPositions(position);
    }

    public void setRandomPlacement(PlayerType player) {
        getPlayer(player).setRandomPlacement();
    }

    public boolean isLastSelect(PlayerType player) {
        return getOpponent(player).isLastSelect();
    }

    public int processSelectedPositions(PlayerType player) {
        return getOpponent(player).processSelectedPositions();
    }

    public boolean didGameEnd(PlayerType player) {
        return getOpponent(player).allShipsDestroyed();
    }

    public Position getRandomPlayPosition(PlayerType player) {
        List<Position> positions = getOpponent(player).getUnknownPositions();

        Random random = new Random();

        if (positions.size() > 0) {
            return positions.get(random.nextInt(positions.size()));
        }

        return null;
    }

    public User getCurrentUser(PlayerType player) {
        return getPlayer(player).getUser();
    }

    public int getNumPlays() {
        return numPlays;
    }

    public void incrementNumPlays() {
        numPlays++;
    }

    public User getUser(PlayerType player) {
        return getPlayer(player).getUser();
    }

    public int getNumberOfHits(PlayerType player) {
        return getOpponent(player).getNumberOfHits();
    }

    public int getShipsDestroyed(PlayerType player) {
        return getPlayer(player).getShipsDestroyed();
    }

    public Board getPlayerBoard(PlayerType type) {
        return getPlayer(type).getBoard();
    }

    public void setPlayerBoard(PlayerType type, Board board) {
        getPlayer(type).setBoard(board);
    }

    public boolean shipIsIntact(PlayerType player, Position position) {
        return getPlayer(player).shipIsIntact(position);
    }

    public void removeOldAttempts(PlayerType player) {
        getPlayer(player).removeOldAttempts();
    }

    public boolean canRepositionShip(PlayerType player) {
        return getPlayer(player).canRepositionShip();
    }

    public PositionType getPositionValidityOnReposition(PlayerType player, Position position, Ship currentShip) {
        return getPlayer(player).getPositionValidityOnReposition(position, currentShip);
    }

    public void hideDestroyedShips(PlayerType player) {
        getPlayer(player).hideDestroyedShips();
    }
}
