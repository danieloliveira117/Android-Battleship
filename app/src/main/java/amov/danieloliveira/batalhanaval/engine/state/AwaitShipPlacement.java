package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidShipNumberException;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.Ship;

class AwaitShipPlacement extends StateAdapter {
    public AwaitShipPlacement(GameData gameData) {
        super(gameData);
    }

    @Override
    public IGameState confirmShipPlacement(PlayerType player) {
        gameData.confirmShipPlacement(player);

        // TODO: 09/08/2018 Change game phase if both players already confirmed
        /*if(gameData.allShipsPlaced()) {
            return new State;
        }*/

        return this;
    }

    @Override
    public IGameState placeShip(PlayerType player, Position position, Integer tag) {
        try {
            gameData.setCurrentShip(player, tag);
            gameData.setCurrentShipPosition(player, position);
        } catch (InvalidShipNumberException e) {
            e.printStackTrace();
        }

        return this;
    }
}
