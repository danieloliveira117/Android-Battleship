package amov.danieloliveira.batalhanaval.engine.model;

import java.util.List;

import amov.danieloliveira.batalhanaval.engine.enums.PositionType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidShipNumberException;

public class Player {
    private boolean isHuman;
    private User user;
    private Board board;
    private boolean shipsPlaced;

    public Player(boolean isHuman) {
        this.isHuman = isHuman;
        this.shipsPlaced = false;
        this.board = new Board();
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    public Board getBoard() {
        return board;
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

    public boolean addNewAttempt(Position position) {
        return board.addNewAttempt(position);
    }

    public void processSelectedPositions() {
        board.processSelectedPositions();
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
}
