package amov.danieloliveira.batalhanaval.engine.model;

import amov.danieloliveira.batalhanaval.Utils;

public class UserBase64 {
    private String username;
    private String image;

    public UserBase64(User user) {
        this.username = user.getUsername();
        this.image = Utils.getStringFromBitmap(user.getImage());
    }

    public User toUser() {
        return new User(username, Utils.getBitmapFromString(this.image));
    }
}
