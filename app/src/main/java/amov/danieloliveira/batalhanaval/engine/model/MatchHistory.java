package amov.danieloliveira.batalhanaval.engine.model;

import amov.danieloliveira.batalhanaval.engine.GameModel;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;

public class MatchHistory {
    private GameMode gameMode;
    private String player;
    private String opponent;
    private int numHitsPlayer;
    private int numHitsOpponent;
    private int shipsDestroyedPlayer;
    private int shipsDestroyedOpponent;
    private boolean playerWon;

    public MatchHistory(GameModel model, User user) {
        PlayerType player = model.getPlayerType(user);
        PlayerType opponent = player == PlayerType.PLAYER ? PlayerType.ADVERSARY : PlayerType.PLAYER;

        this.gameMode = model.getGameMode();

        this.player = model.getUser(player).getUsername();
        this.opponent = model.getUser(opponent).getUsername();

        this.numHitsPlayer = model.getNumberOfHits(player);
        this.shipsDestroyedPlayer = model.getShipsDestroyed(opponent);

        this.numHitsOpponent = model.getNumberOfHits(opponent);
        this.shipsDestroyedOpponent = model.getShipsDestroyed(player);

        this.playerWon = model.didGameEnd(player);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getPlayer() {
        return player;
    }

    public String getOpponent() {
        return opponent;
    }

    public int getNumHitsPlayer() {
        return numHitsPlayer;
    }

    public int getNumHitsOpponent() {
        return numHitsOpponent;
    }

    public int getShipsDestroyedPlayer() {
        return shipsDestroyedPlayer;
    }

    public int getShipsDestroyedOpponent() {
        return shipsDestroyedOpponent;
    }

    public boolean didPlayerWin() {
        return playerWon;
    }
}
