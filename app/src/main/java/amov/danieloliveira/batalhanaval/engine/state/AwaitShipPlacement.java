package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidShipNumberException;
import amov.danieloliveira.batalhanaval.engine.model.Board;
import amov.danieloliveira.batalhanaval.engine.model.Position;

public class AwaitShipPlacement extends GameStateAdapter {
    public AwaitShipPlacement(GameData gameData) {
        super(gameData);
    }

    @Override
    public IGameState confirmShipPlacementRemote(PlayerType type, Board board) {
        gameData.setPlayerBoard(type, board);
        return confirmShipPlacement(type);
    }

    @Override
    public IGameState confirmShipPlacement(PlayerType player) {
        gameData.setShipsPlaced(player);

        if (gameData.allShipsPlaced()) {
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

    @Override
    public IGameState rotateCurrentShip(PlayerType player) {
        gameData.rotateShip(player);
        return this;
    }
}
