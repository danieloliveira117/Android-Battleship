package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.Ship;
import amov.danieloliveira.batalhanaval.engine.model.User;

public interface IGameState {
    IGameState startGame(GameMode mode, User user);

    IGameState setAdversary(User user);

    IGameState confirmShipPlacement(PlayerType player);

    IGameState placeShip(PlayerType player, Position position, Integer tag);

    IGameState moveShip(PlayerType player, Position oldposition, Position newposition);

    IGameState setCurrentShip(PlayerType player, Position position);

    IGameState randomizePlacement(PlayerType player);

    IGameState clickNewPosition(PlayerType player, Position position);
}
