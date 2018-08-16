package amov.danieloliveira.batalhanaval.engine.state;

import amov.danieloliveira.batalhanaval.engine.GameData;

// TODO: 16/08/2018 Remove MISS attempts after ship has been moved
class AwaitShipReposition extends GameStateAdapter {
    public AwaitShipReposition(GameData gameData) {
        super(gameData);
    }
}
