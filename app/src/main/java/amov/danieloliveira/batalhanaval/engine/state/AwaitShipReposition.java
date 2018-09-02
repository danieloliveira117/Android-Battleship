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

    private void trySelectShip(PlayerType player, Position position) {
        if (gameData.shipIsIntact(player, position)) { // Select new ship to reposition
            gameData.setCurrentShipByPosition(player, position);

            Ship ship = gameData.getCurrentShip(player);

            originalPosition = new Position(ship.getPositionList().get(0));
            originalOrientation = ship.getOrientation();
        }
    }

    @Override
    public IGameState setCurrentShipByPosition(PlayerType player, Position position) {
        if (gameData.getCurrentPlayer() == player) {
            Ship ship = gameData.getCurrentShip(player);

            if (ship != null) {
                // If current ship is in its original position / orientation
                if (ship.getPositionList().get(0).equals(originalPosition) && ship.getOrientation().equals(originalOrientation)) {
                    gameData.clearCurrentShip(player);

                    if (!gameData.currentShipContainsPosition(player, position)) {
                        trySelectShip(player, position);
                    }
                }
            } else {
                trySelectShip(player, position);
            }
        }

        return this;
    }

    @Override
    public IGameState setShipOnDragEvent(PlayerType player, Position position) {
        if (gameData.getCurrentPlayer() == player) {
            Ship ship = gameData.getCurrentShip(player);

            if (ship != null) {
                // If current ship is in its original position / orientation
                if (ship.getPositionList().get(0).equals(originalPosition) && ship.getOrientation().equals(originalOrientation)) {
                    if (!gameData.currentShipContainsPosition(player, position)) {
                        trySelectShip(player, position);
                    }
                }
            } else {
                trySelectShip(player, position);
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

    public boolean canDragAndDrop(PlayerType type, Position position) {
        if (gameData.getCurrentPlayer().equals(type)) {
            Ship ship = gameData.getCurrentShip(type);

            if (ship != null) {
                // Is CurrentShip or CurrentShip is in original position
                return gameData.currentShipContainsPosition(type, position) ||
                        ship.getPositionList().get(0).equals(originalPosition) && ship.getOrientation().equals(originalOrientation);
            } else {
                return gameData.shipIsIntact(type, position);
            }
        }

        return false;
    }
}
