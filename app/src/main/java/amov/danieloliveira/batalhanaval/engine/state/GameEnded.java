package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;

class GameEnded extends GameStateAdapter {
    public GameEnded(GameData gameData) {
        super(gameData);

        gameData.saveGameResults();
    }
}
