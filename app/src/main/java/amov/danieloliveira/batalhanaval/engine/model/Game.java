package amov.danieloliveira.batalhanaval.engine.model;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;

public class Game {
    private Player player1, player2;
    private GameMode gameMode;

    public Game() {
        player1 = new Player(false);
        player2 = new Player(true);
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}
