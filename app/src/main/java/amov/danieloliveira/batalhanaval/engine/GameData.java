package amov.danieloliveira.batalhanaval.engine;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.model.User;
import amov.danieloliveira.batalhanaval.engine.state.AwaitGameStart;
import amov.danieloliveira.batalhanaval.engine.state.IGameState;

public class GameData {
    private GameModel gameModel;
    private IGameState currentState;

    GameData() {
        this.gameModel = new GameModel();
        this.currentState = new AwaitGameStart(this);
    }

    /* States */
    public void startGame(GameMode mode, User user) {
        currentState = currentState.startGame(mode, user);
    }

    public void setAdversary(User user) {
        currentState = currentState.setAdversary(user);
    }

    /* Update Game Model */
    public void prepareGame(GameMode mode) {
        gameModel.setMode(mode);

        // TODO: 07/08/2018 anything else needed here?
    }

    public void updatePlayerData(int player, User user) {
        gameModel.updatePlayerData(player, user);
    }
}
