package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Ship;
import amov.danieloliveira.batalhanaval.engine.model.User;

public interface IGameState {
    IGameState startGame(GameMode mode, User user);

    IGameState setAdversary(User user);

    IGameState nextShip(PlayerType player);

    IGameState confirmShipPlacement(PlayerType player);
}
