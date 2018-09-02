package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.Orientation;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Board;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.Ship;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class AwaitShipReposition extends GameStateAdapter {
    private Position originalPosition = null;
    private Orientation originalOrientation = null;

    AwaitShipReposition(GameData gameData) {
        super(gameData);

        gameData.clearCurrentShip(gameData.getCurrentPlayer());
        gameData.hideDestroyedShips(gameData.getCurrentPlayer());
    }

    @Override
    public IGameState setCurrentShipByPosition(PlayerType player, Position position) {
        if (gameData.getCurrentPlayer() == player) {
            if (gameData.getCurrentShip(player) == null && gameData.shipIsIntact(player, position)) { // Select new ship to reposition
                gameData.setCurrentShipByPosition(player, position);

                Ship ship = gameData.getCurrentShip(player);

                originalPosition = new Position(ship.getPositionList().get(0));
                originalOrientation = ship.getOrientation();
            } else if (gameData.getCurrentShip(player) != null) { // Clear selected ship
                Ship ship = gameData.getCurrentShip(player);

                if (gameData.allShipsPlaced(player) &&
                        ship.getPositionList().get(0).equals(originalPosition)
                        && ship.getOrientation().equals(originalOrientation)) {

                    gameData.clearCurrentShip(player);
                }
            }
        }

        return this;
    }

    @Override
    public IGameState moveCurrentShip(PlayerType player, Position new_position) {
        gameData.setCurrentShipPosition(player, new_position);
        gameData.setShipsPlaced(player);

        return this;
    }

    @Override
    public IGameState rotateCurrentShip(PlayerType player) {
        gameData.rotateShip(player);
        gameData.setShipsPlaced(player);

        return this;
    }

    @Override
    public IGameState confirmShipPlacement(PlayerType player) {
        if (gameData.getCurrentPlayer() == player) {
            gameData.setShipsPlaced(player);

            if (gameData.allShipsPlaced(player)) {
                gameData.removeOldAttempts(player);
                gameData.nextPlayer();

                return new AwaitPlayerMove(gameData);
            }
        }

        return this;
    }

    @Override
    public IGameState confirmShipPlacementRemote(PlayerType type, Board board) {
        if (gameData.getCurrentPlayer() == type) {
            gameData.setPlayerBoard(type, board);

            gameData.setShipsPlaced(type);

            if (gameData.allShipsPlaced(type)) {
                gameData.nextPlayer();

                return new AwaitPlayerMove(gameData);
            }
        }

        return this;
    }

    @Override
    public IGameState setShipOnDragEvent(PlayerType player, Position position) {
        if (gameData.getCurrentPlayer() == player && gameData.getCurrentShip(player) == null && gameData.shipIsIntact(player, position)) { // Select new ship to reposition
            gameData.setCurrentShipByPosition(player, position);

            Ship ship = gameData.getCurrentShip(player);

            originalPosition = new Position(ship.getPositionList().get(0));
            originalOrientation = ship.getOrientation();
        }

        return this;
    }

    @Override
    public IGameState restorePosition(PlayerType player) {
        if (gameData.getCurrentPlayer() == player && originalPosition != null && originalOrientation != null) {
            Ship ship = gameData.getCurrentShip(player);

            if (ship != null) {
                ship.updatePosition(originalPosition);
                ship.setOrientation(originalOrientation);
            }
        }

        return this;
    }

    @Override
    public IGameState setUser(PlayerType opponent, User user) {
        gameData.updatePlayerData(opponent, user);
        return this;
    }

    @Override
    public IGameState changeToSinglePlayerMode(PlayerType player) {
        gameData.setGameMode(GameMode.vsAI);
        return this;
    }
}
