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

    public PositionType getPositionType(Position position) {
        return board.getPositionType(position);
    }

    public Ship getShipByID(Integer ship) throws InvalidShipNumberException {
        return board.getShipByID(ship);
    }

    public Ship getShipByPosition(Position position) {
        return board.getShipByPosition(position);
    }

    public PositionType getPositionValidity(Position position) {
        return board.getPositionValidity(position);
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
}
