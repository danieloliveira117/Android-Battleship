package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;

public interface IGameState {
    IGameState startGame(GameMode mode);
}
