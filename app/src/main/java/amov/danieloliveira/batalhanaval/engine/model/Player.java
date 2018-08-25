package amov.danieloliveira.batalhanaval.engine.model;

import java.util.List;

import amov.danieloliveira.batalhanaval.engine.enums.PositionType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidShipNumberException;

public class Player {
    private User user;
    private Board board;
    private boolean shipsPlaced;

    public Player() {
        this.shipsPlaced = false;
        this.board = new Board();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean allShipsPlaced() {
        return shipsPlaced;
    }

    public void setShipsPlaced() {
        this.shipsPlaced = !board.hasInvalidShipPositions();
    }

    public void addNewAttempt(Position position) {
        board.addNewAttempt(position);
    }

    public int processSelectedPositions() {
        return board.processSelectedPositions();
    }

    /* --- Position Type --- */
    public PositionType getPositionType(Position position) {
        return board.getPositionType(position);
    }

    public PositionType getPositionValidity(Position position, Ship currentShip) {
        return board.getPositionValidity(position, currentShip);
    }

    public PositionType getPositionValidityOnReposition(Position position, Ship currentShip) {
        return board.getPositionValidityOnReposition(position, currentShip);
    }
    /* --- End Position Type --- */

    public Ship getShipByID(Integer ship) throws InvalidShipNumberException {
        return board.getShipByID(ship);
    }

    public Ship getShipByPosition(Position position) {
        return board.getShipByPosition(position);
    }

    public List<Position> getShipPositions(Position position) {
        return board.getShipPositions(position);
    }

    public void setRandomPlacement() {
        board.setRandomPlacement();
    }

    public boolean isLastSelect() {
        return board.isLastSelect();
    }

    public boolean allShipsDestroyed() {
        return board.allShipsDestroyed();
    }

    public List<Position> getUnknownPositions() {
        return board.getUnknownPositions();
    }

    public int getShipsDestroyed() {
        return board.getShipsDestroyed();
    }

    public int getNumberOfHits() {
        return board.getNumberOfHits();
    }

    public boolean shipIsIntact(Position position) {
        return board.shipIsIntact(getShipByPosition(position));
    }

    public void removeOldAttempts() {
        board.removeOldAttempts();
    }

    public boolean canRepositionShip() {
        return board.canRepositionShip();
    }
}
