package amov.danieloliveira.batalhanaval.engine;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.model.Game;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.ADVERSARY;
import static amov.danieloliveira.batalhanaval.Consts.PLAYER;

class GameModel {
    private Game game = new Game();

    public void updatePlayerData(int player, User user) {
        switch (player) {
            case PLAYER:
                game.getPlayer1().setUser(user);
                break;
            case ADVERSARY:
                game.getPlayer2().setUser(user);
                game.getPlayer2().setHuman(true);
                break;
        }
    }

    public void setMode(GameMode mode) {
        game.setGameMode(mode);
    }
}
