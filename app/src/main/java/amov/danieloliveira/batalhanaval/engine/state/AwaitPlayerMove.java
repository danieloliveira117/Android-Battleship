package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Position;

import static amov.danieloliveira.batalhanaval.Consts.MAXSELECT;

public class AwaitPlayerMove extends GameStateAdapter {
    public AwaitPlayerMove(GameData gameData) {
        super(gameData);
    }

    @Override
    public IGameState clickNewPosition(PlayerType player, Position position) {
        if(gameData.getCurrentPlayer() == player) {
            gameData.addNewAttempt(position);

            if(gameData.isLastSelect()) {
                // TODO Mudar a posição de um barco
                if(gameData.processSelectedPositions() == MAXSELECT) {
                    // return new AwaitShipReposition(gameData);
                }

                if(gameData.didGameEnd()) {
                    return new GameEnded(gameData);
                } else {
                    gameData.nextPlayer();
                }
            }
        }

        return this;
    }
}
