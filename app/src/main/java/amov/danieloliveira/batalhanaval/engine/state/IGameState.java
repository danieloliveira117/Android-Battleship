package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.model.User;

public interface IGameState {
    IGameState startGame(GameMode mode, User user);

    IGameState setAdversary(User user);
}
