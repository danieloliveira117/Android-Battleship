package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;

public class AwaitGameStart extends StateAdapter {

    public AwaitGameStart(GameData gameData) {
        super(gameData);
    }

    @Override
    public IGameState startGame(GameMode mode) {
        gameData.prepareGame(mode);
        return new AwaitShipPlacement(gameData);
    }
}
