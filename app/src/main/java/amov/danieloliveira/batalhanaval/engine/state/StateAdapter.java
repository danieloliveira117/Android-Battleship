package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class StateAdapter implements IGameState {
    GameData gameData;

    public StateAdapter(GameData gameData) {
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
}
