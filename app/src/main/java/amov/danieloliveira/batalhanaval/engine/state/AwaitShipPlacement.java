package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Ship;

class AwaitShipPlacement extends StateAdapter {
    public AwaitShipPlacement(GameData gameData) {
        super(gameData);
    }

    @Override
    public IGameState nextShip(PlayerType player) {
        boolean next = false;
        Ship[] ships = gameData.getPlayerShips(player);
        Ship current = gameData.getCurrentShip(player);

        if (current == null) {
            gameData.setCurrentShip(player, ships[0]);
        } else {
            for (Ship ship : ships) {
                if (ship == current) {
                    next = true;
                } else if (next) {
                    break;
                }
            }
        }

        return this;
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
}
