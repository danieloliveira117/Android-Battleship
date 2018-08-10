package amov.danieloliveira.batalhanaval.engine.exceptions;

public class InvalidPositionException extends BattleshipException {
    public InvalidPositionException() {
        super("Invalid Position - must be between A1 and H8");
    }
}
