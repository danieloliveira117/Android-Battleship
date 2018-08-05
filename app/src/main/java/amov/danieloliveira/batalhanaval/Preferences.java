package amov.danieloliveira.batalhanaval;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

import amov.danieloliveira.batalhanaval.engine.model.User;

public class Preferences {
    public static final String IMAGE_NAME = "/image.jpg";

    public static void loadPreferences(Activity activity) {
        BattleshipApplication app = (BattleshipApplication) activity.getApplication();
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        String username = sharedPref.getString("username", null);

        File file = new File(Environment.getExternalStorageDirectory() + IMAGE_NAME);
        Bitmap image = null;

        if (file.exists()) {
            image = BitmapFactory.decodeFile(file.getAbsolutePath());
        }

        app.setUser(new User(username, image));

        if (username != null) {
            Toast.makeText(activity, "Olá " + username, Toast.LENGTH_LONG).show();
        }
    }

    public static void savePreferences(Activity activity) {
        BattleshipApplication app = (BattleshipApplication) activity.getApplication();
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", app.getUser().getUsername());
        editor.apply();

        Toast.makeText(activity, "Dados do utilizador guardados.", Toast.LENGTH_LONG).show();
    }
}
