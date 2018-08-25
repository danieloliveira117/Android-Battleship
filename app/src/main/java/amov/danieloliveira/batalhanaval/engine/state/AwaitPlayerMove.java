package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Position;

import static amov.danieloliveira.batalhanaval.Consts.MAXSELECT;

public class AwaitPlayerMove extends GameStateAdapter {
    AwaitPlayerMove(GameData gameData) {
        super(gameData);
    }

    @Override
    public IGameState clickNewPosition(PlayerType player, Position position) {
        if (gameData.getCurrentPlayer() == player && position != null) {
            gameData.addNewAttempt(position);

            if (gameData.isLastSelect()) {
                int hits = gameData.processSelectedPositions();

                gameData.incrementNumPlays();

                if (gameData.didGameEnd()) {
                    return new GameEnded(gameData);
                }

                // Must have one undiscovered ship
                if (hits == MAXSELECT && gameData.canRepositionShip(player)) {
                    return new AwaitShipReposition(gameData);
                }

                gameData.nextPlayer();
            }
        }

        return this;
    }

    @Override
    public IGameState clickPositionRemote(PlayerType type, Position position) {
        return clickNewPosition(type, position);
    }
}
