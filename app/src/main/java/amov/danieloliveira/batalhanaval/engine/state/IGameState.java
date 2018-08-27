package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Board;
import amov.danieloliveira.batalhanaval.engine.model.Position;
import amov.danieloliveira.batalhanaval.engine.model.User;

public interface IGameState {
    IGameState startGame(GameMode mode, User user, boolean client);

    IGameState setAdversary(User user);

    IGameState confirmShipPlacement(PlayerType player);

    IGameState placeShip(PlayerType player, Position position, Integer tag);

    IGameState moveCurrentShip(PlayerType player, Position newposition);

    IGameState setCurrentShipByPosition(PlayerType player, Position position);

    IGameState randomizePlacement(PlayerType player);

    IGameState clickNewPosition(PlayerType player, Position position);

    IGameState rotateCurrentShip(PlayerType player);

    IGameState confirmShipPlacementRemote(PlayerType type, Board board);

    IGameState setStartingPlayer(PlayerType type);

    IGameState clickPositionRemote(PlayerType type, Position position);

    IGameState setShipOnDragEvent(PlayerType type, Position position);

    IGameState restorePosition(PlayerType player);
}
