package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Board;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class GameStateAdapter implements IGameState {
    GameData gameData;

    public GameStateAdapter(GameData gameData) {
        this.gameData = gameData;
    }

    @Override
    public IGameState startGame(GameMode mode, User user) {
        return this;
    }

    @Override
    public IGameState setAdversary(User user) {
        return this;
    }

    @Override
    public IGameState confirmShipPlacement(PlayerType player) {
        return this;
    }

    @Override
    public IGameState placeShip(PlayerType player, Position position, Integer tag) {
        return this;
    }

    @Override
    public IGameState moveShip(PlayerType player, Position old_position, Position new_position) {
        return this;
    }

    @Override
    public IGameState setCurrentShip(PlayerType player, Position position) {
        return this;
    }

    @Override
    public IGameState randomizePlacement(PlayerType player) {
        return this;
    }

    @Override
    public IGameState clickNewPosition(PlayerType player, Position position) {
        return this;
    }

    @Override
    public IGameState rotateCurrentShip(PlayerType player) {
        return this;
    }

    @Override
    public IGameState confirmShipPlacementRemote(PlayerType type, Board board) {
        return this;
    }
}
