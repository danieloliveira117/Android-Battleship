package amov.danieloliveira.batalhanaval.engine.exceptions;

import static amov.danieloliveira.batalhanaval.Consts.MAXSHIPS;

public class InvalidShipNumberException extends BattleshipException {
    public InvalidShipNumberException() {
        super("Invalid Ship Number - must be between " + 0 + " and " + MAXSHIPS);
    }
}
