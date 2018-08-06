package amov.danieloliveira.batalhanaval.engine.model;

import android.graphics.Bitmap;

public class User {
    private String username;
    private Bitmap image;
    private String ip;

    public User(String username, Bitmap image) {
        this.username = username;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
