package amov.danieloliveira.batalhanaval.engine.enums;

public enum PlayerType {
    PLAYER,
    ADVERSARY;

    public PlayerType getOpponent() {
        if (this == PlayerType.ADVERSARY)
            return PlayerType.PLAYER;
        else
            return PlayerType.ADVERSARY;
    }
}
