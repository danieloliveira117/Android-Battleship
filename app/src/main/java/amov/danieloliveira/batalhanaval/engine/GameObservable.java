package amov.danieloliveira.batalhanaval.engine;

import java.util.List;
import java.util.Observable;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.MsgType;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.enums.PositionType;
import amov.danieloliveira.batalhanaval.engine.model.Board;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.Ship;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.BOT_NAME;
import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;
import static amov.danieloliveira.batalhanaval.engine.enums.MsgType.CONFIRM_PLACEMENT;

public class GameObservable extends Observable {
    private GameData gameData;
    private BattleshipApplication app;

    public GameObservable(BattleshipApplication app) {
        this.app = app;
        this.gameData = new GameData(app);
    }

    public void newGameData() {
        this.gameData = new GameData(app);
    }

    /* --- SETS --- */
    public void refreshData() {
        setChanged();
        notifyObservers(gameData.getGameMode());
    }

    public void startGame(GameMode mode, User user, boolean client) {
        gameData.startGame(mode, user, client);

        setChanged();
        notifyObservers(mode);
    }

    public void setAdversaryUser(User user) {
        gameData.setAdversary(user);

        setChanged();
        notifyObservers();
    }

    public void clickNewPosition(PlayerType player, Position position) {
        gameData.clickNewPosition(player, position);

        setChanged();
        notifyObservers(position);
    }

    public void placeShip(PlayerType player, Position position, Integer tag) {
        gameData.placeShip(player, position, tag);

        setChanged();
        notifyObservers();
    }

    public void moveShip(PlayerType player, Position new_position) {
        gameData.moveShip(player, new_position);

        setChanged();
        notifyObservers();
    }

    public void confirmPlacement(PlayerType player) {
        gameData.confirmShipPlacement(player);

        setChanged();
        notifyObservers(CONFIRM_PLACEMENT);
    }

    public void selectShip(PlayerType player, Position position) {
        gameData.setCurrentShip(player, position);

        setChanged();
        notifyObservers();
    }

    public void randomizePlacement(PlayerType player) {
        gameData.randomizePlacement(player);

        setChanged();
        notifyObservers();
    }


    public void playRandomly(PlayerType type) {
        gameData.playRandomly(type);

        setChanged();
        notifyObservers();
    }

    public void rotateCurrentShip(PlayerType player) {
        gameData.rotateCurrentShip(player);

        setChanged();
        notifyObservers();
    }

    public void confirmPlacementRemote(PlayerType type, Board board) {
        gameData.confirmShipPlacementRemote(type, board);

        setChanged();
        notifyObservers(board);
    }

    public void sendStartingPlayer() {
        setChanged();
        notifyObservers(MsgType.STARTING_PLAYER);
    }

    public void clickPositionRemote(PlayerType type, Position position) {
        gameData.clickPositionRemote(type, position);

        setChanged();
        notifyObservers();
    }

    public void setStartingPlayerRemote(PlayerType type) {
        gameData.setStartingPlayer(type);

        setChanged();
        notifyObservers();
    }

    public void setShipOnDragEvent(PlayerType type, Position position) {
        gameData.setShipOnDragEvent(type, position);

        setChanged();
        notifyObservers();
    }

    public void restorePosition(PlayerType player) {
        gameData.restorePosition(player);

        setChanged();
        notifyObservers();
    }

    public void changeToSinglePlayerMode(PlayerType player) {
        gameData.changeToSinglePlayerMode(player);

        setChanged();
        notifyObservers(BOT_NAME);
    }

    /* --- GETS --- */

    public PositionType getPositionType(PlayerType player, Position position) {
        return gameData.getPositionType(player, position);
    }

    public List<Position> getShipPositions(PlayerType player, Position position) {
        return gameData.getShipPositions(player, position);
    }

    public User getPlayerUser(PlayerType type) {
        return gameData.getPlayerUser(type);
    }

    public boolean validPlacement(PlayerType player) {
        return gameData.allShipsPlaced(player);
    }

    public PlayerType getCurrentPlayer() {
        return gameData.getCurrentPlayer();
    }

    public boolean canDragAndDrop(PlayerType type, Position position) {
        return gameData.canDragAndDrop(type, position);
    }

    public boolean didGameEnd() {
        return gameData.didGameEnd();
    }

    public User getCurrentUser() {
        return gameData.getCurrentUser();
    }

    public Board getPlayerBoard(PlayerType type) {
        return gameData.getPlayerBoard(type);
    }

    public Ship getCurrentShip(PlayerType type) {
        return gameData.getCurrentShip(type);
    }

    public boolean isShipReposition(PlayerType type) {
        return gameData.isShipReposition(type);
    }

    public void setUser(PlayerType opponent, User user) {
        gameData.setUser(opponent, user);

        setChanged();
        notifyObservers();
    }
}
