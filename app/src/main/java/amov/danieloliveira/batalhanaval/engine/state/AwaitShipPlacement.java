package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidShipNumberException;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.Ship;

public class AwaitShipPlacement extends StateAdapter {
    public AwaitShipPlacement(GameData gameData) {
        super(gameData);
    }

    @Override
    public IGameState confirmShipPlacement(PlayerType player) {
        gameData.setShipsPlaced(player);

        // TODO: 09/08/2018 Change game phase if both players already confirmed
        if(gameData.allShipsPlaced()) {
            gameData.randomizeStartingPlayer();
            return new AwaitPlayerMove(gameData);
        }

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

    @Override
    public IGameState moveShip(PlayerType player, Position old_position, Position new_position) {
        gameData.setCurrentShipPosition(player, old_position);
        gameData.setCurrentShipPosition(player, new_position);

        return this;
    }

    @Override
    public IGameState setCurrentShip(PlayerType player, Position position) {
        gameData.setCurrentShipByPosition(player, position);

        return this;
    }

    @Override
    public IGameState randomizePlacement(PlayerType player) {
        gameData.setRandomPlacement(player);
        return this;
    }
}
