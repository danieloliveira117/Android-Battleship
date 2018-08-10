package amov.danieloliveira.batalhanaval.engine.model;

public class Player {
    private boolean isHuman;
    private User user;
    private Board board;
    private boolean shipsPlaced;

    Player(boolean isHuman) {
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

    public void setShipsPlaced(boolean shipsPlaced) {
        this.shipsPlaced = shipsPlaced;
    }
}
