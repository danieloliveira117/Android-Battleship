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
    private int numPlays;
    private boolean playerWon;

    public MatchHistory(GameModel model, User user) {
        PlayerType playerType = model.getUser(PlayerType.PLAYER).equals(user) ? PlayerType.PLAYER : PlayerType.ADVERSARY;
        PlayerType opponentType = playerType == PlayerType.PLAYER ? PlayerType.ADVERSARY : PlayerType.PLAYER;

        this.gameMode = model.getGameMode();

        this.numPlays = model.getNumPlays();

        this.player = model.getUser(playerType).getUsername();
        this.opponent = model.getUser(opponentType).getUsername();

        this.numHitsPlayer = model.getNumberOfHits(playerType);
        this.shipsDestroyedPlayer = model.getShipsDestroyed(opponentType);

        this.numHitsOpponent = model.getNumberOfHits(opponentType);
        this.shipsDestroyedOpponent = model.getShipsDestroyed(playerType);

        this.playerWon = model.didGameEnd(playerType);
    }

    public int getNumPlays() {
        return numPlays;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getPlayerName() {
        return player;
    }

    public String getOpponentName() {
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
