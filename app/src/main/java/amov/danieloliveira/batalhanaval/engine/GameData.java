package amov.danieloliveira.batalhanaval.engine;

import java.util.List;
import java.util.Random;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.enums.PositionType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidShipNumberException;
import amov.danieloliveira.batalhanaval.engine.model.Board;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.Ship;
import amov.danieloliveira.batalhanaval.engine.model.User;
import amov.danieloliveira.batalhanaval.engine.state.AwaitGameStart;
import amov.danieloliveira.batalhanaval.engine.state.AwaitShipPlacement;
import amov.danieloliveira.batalhanaval.engine.state.IGameState;

public class GameData {
    private BattleshipApplication app;
    private PlayerType currentPlayer;
    private GameModel gameModel;
    private IGameState currentState;
    private Ship[] currentShip = {null, null};

    GameData(BattleshipApplication application) {
        this.app = application;
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

    public void placeShip(PlayerType player, Position position, Integer tag) {
        currentState = currentState.placeShip(player, position, tag);
    }

    public void confirmShipPlacement(PlayerType player) {
        currentState = currentState.confirmShipPlacement(player);
    }

    public void moveShip(PlayerType player, Position oldposition, Position newposition) {
        currentState = currentState.moveShip(player, oldposition, newposition);
    }

    public void setCurrentShip(PlayerType player, Position position) {
        currentState = currentState.setCurrentShip(player, position);
    }

    public void randomizePlacement(PlayerType player) {
        currentState = currentState.randomizePlacement(player);
    }

    public void clickNewPosition(PlayerType player, Position position) {
        currentState = currentState.clickNewPosition(player, position);
    }

    public void rotateCurrentShip(PlayerType player) {
        currentState = currentState.rotateCurrentShip(player);
    }

    public void confirmShipPlacementRemote(PlayerType type, Board board) {
        currentState = currentState.confirmShipPlacementRemote(type, board);
    }

    public void setStartingPlayer(PlayerType type) {
        currentState = currentState.setStartingPlayer(type);
    }

    public void clickPositionRemote(PlayerType type, Position position) {
        currentState = currentState.clickPositionRemote(type, position);
    }

    /* Update Game Model */
    public void prepareGame(GameMode mode) {
        gameModel.setMode(mode);

        if (mode == GameMode.vsAI) {
            gameModel.setRandomPlacement(PlayerType.ADVERSARY);
            gameModel.setShipsPlaced(PlayerType.ADVERSARY);
        }
    }

    public void updatePlayerData(PlayerType player, User user) {
        gameModel.updatePlayerData(player, user);
    }

    /* Gets */
    public PlayerType getCurrentPlayer() {
        return currentPlayer;
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

    public void setCurrentShip(PlayerType player, Integer ship) throws InvalidShipNumberException {
        Ship temp = gameModel.getPlayerShip(player, ship);

        switch (player) {
            case PLAYER:
                currentShip[0] = temp;
            case ADVERSARY:
                currentShip[1] = temp;
        }
    }

    public boolean allShipsPlaced() {
        return gameModel.allShipsPlaced();
    }

    public boolean allShipsPlaced(PlayerType playerType) {
        return gameModel.allShipsPlaced(playerType);
    }

    public void addNewAttempt(Position position) {
        gameModel.addNewAttempt(currentPlayer, position);
    }

    public PositionType getPositionType(PlayerType player, Position position) {
        if (currentState instanceof AwaitGameStart)
            return PositionType.UNKNOWN;

        if (currentState instanceof AwaitShipPlacement)
            return gameModel.getPositionValidity(player, position, getCurrentShip(player));

        return gameModel.getPositionType(player, position);
    }

    public void setRandomPlacement(PlayerType player) {
        gameModel.setRandomPlacement(player);
    }

    public void setCurrentShipPosition(PlayerType player, Position position) {
        getCurrentShip(player).updatePosition(position);
    }

    public void setShipsPlaced(PlayerType player) {
        gameModel.setShipsPlaced(player);
    }

    public void randomizeStartingPlayer() {
        Random r = new Random();
        if (r.nextBoolean()) {
            currentPlayer = PlayerType.PLAYER;
        } else {
            currentPlayer = PlayerType.ADVERSARY;
        }
    }

    public void nextPlayer() {
        if (currentPlayer == PlayerType.PLAYER) {
            currentPlayer = PlayerType.ADVERSARY;
        } else {
            currentPlayer = PlayerType.PLAYER;
        }
    }

    public List<Position> getShipPositions(PlayerType player, Position position) {
        return gameModel.getShipPositions(player, position);
    }

    public void setCurrentShipByPosition(PlayerType player, Position position) {
        Ship temp = gameModel.getPlayerShip(player, position);

        switch (player) {
            case PLAYER:
                currentShip[0] = temp;
            case ADVERSARY:
                currentShip[1] = temp;
        }
    }

    public User getPlayer(PlayerType player) {
        return gameModel.getPlayer(player).getUser();
    }

    public GameMode getGameMode() {
        return gameModel.getGameMode();
    }

    public boolean canDragAndDrop() {
        return currentState instanceof AwaitShipPlacement;
    }

    public boolean isLastSelect() {
        return gameModel.isLastSelect(currentPlayer);
    }

    public int processSelectedPositions() {
        return gameModel.processSelectedPositions(currentPlayer);
    }

    public boolean didGameEnd() {
        return gameModel.didGameEnd(currentPlayer);
    }

    public void playRandomly() {
        clickNewPosition(PlayerType.ADVERSARY, gameModel.getRandomPlayPosition(currentPlayer));
    }

    public User getCurrentUser() {
        return gameModel.getCurrentUser(currentPlayer);
    }

    public void saveGameResults() {
        app.addNewMatchHistory(gameModel);
    }

    public void incrementNumPlays() {
        gameModel.incrementNumPlays();
    }

    public void rotateShip(PlayerType player) {
        gameModel.rotateShip(getCurrentShip(player));
    }

    public Board getPlayerBoard(PlayerType type) {
        return gameModel.getPlayerBoard(type);
    }

    public void setPlayerBoard(PlayerType type, Board board) {
        gameModel.setPlayerBoard(type, board);
    }

    public void setCurrentPlayer(PlayerType currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
