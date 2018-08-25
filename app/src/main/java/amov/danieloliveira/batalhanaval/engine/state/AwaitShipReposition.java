package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.Orientation;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.Ship;

public class AwaitShipReposition extends GameStateAdapter {
    private Position originalPosition = null;
    private Orientation originalOrientation = null;

    AwaitShipReposition(GameData gameData) {
        super(gameData);

        gameData.clearCurrentShip(gameData.getCurrentPlayer());
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
                gameData.removeDestroyedShips(player); // TODO: 25/08/2018 removeDestroyedShips
                gameData.removeOldAttempts(player);
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
}
