package amov.danieloliveira.batalhanaval.engine.model;

import android.graphics.Bitmap;

public class Player {
    private boolean isHuman;
    private String username;
    private Bitmap image;
    private String ip;
    private Board board = new Board();

    public Player(boolean isHuman, String username, Bitmap image) {
        this.isHuman = isHuman;
        this.username = username;
        this.image = image;
        this.ip = null;
    }

    public Player(boolean isHuman, String username, Bitmap image, String ip) {
        this.isHuman = isHuman;
        this.username = username;
        this.image = image;
        this.ip = ip;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public void setHuman(boolean human) {
        isHuman = human;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
