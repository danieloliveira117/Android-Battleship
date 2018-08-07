package amov.danieloliveira.batalhanaval.engine.model;

public class Player {
    private boolean isHuman;
    private User user;
    private Board board = new Board();

    public Player(boolean isHuman) {
        this.isHuman = isHuman;
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

    public void setBoard(Board board) {
        this.board = board;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
