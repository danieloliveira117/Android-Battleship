package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.ADVERSARY;
import static amov.danieloliveira.batalhanaval.Consts.PLAYER;

public class AwaitGameStart extends StateAdapter {

    public AwaitGameStart(GameData gameData) {
        super(gameData);
    }

    @Override
    public IGameState startGame(GameMode mode, User user) {
        gameData.updatePlayerData(PLAYER, user);
        gameData.prepareGame(mode);
        return new AwaitShipPlacement(gameData);
    }

    @Override
    public IGameState setAdversary(User user) {
        gameData.updatePlayerData(ADVERSARY, user);
        return this;
    }
}
