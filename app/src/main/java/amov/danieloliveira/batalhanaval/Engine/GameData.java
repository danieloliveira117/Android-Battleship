package amov.danieloliveira.batalhanaval.engine;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.state.AwaitGameStart;
import amov.danieloliveira.batalhanaval.engine.state.IGameState;

public class GameData {
    private GameModel gameModel;
    private IGameState currentState;

    GameData() {
        this.gameModel = new GameModel();
        this.currentState = new AwaitGameStart(this);
    }

    public void startGame(GameMode mode) {
        currentState = currentState.startGame(mode);
    }

    public void prepareGame(GameMode mode) {

    }
}
