package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class AwaitGameStart extends StateAdapter {

    public AwaitGameStart(GameData gameData) {
        super(gameData);
    }

    @Override
    public IGameState startGame(GameMode mode, User user) {
        gameData.updatePlayerData(PlayerType.PLAYER, user);
        gameData.prepareGame(mode);
        return new AwaitShipPlacement(gameData);
    }

    @Override
    public IGameState setAdversary(User user) {
        gameData.updatePlayerData(PlayerType.ADVERSARY, user);
        return this;
    }
}
