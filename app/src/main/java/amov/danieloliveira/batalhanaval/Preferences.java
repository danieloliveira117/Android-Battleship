package amov.danieloliveira.batalhanaval;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.widget.Toast;

import amov.danieloliveira.batalhanaval.engine.model.User;

public class Preferences {
    public static User loadPreferences(Activity activity) {
        User user = null;

        BattleshipApplication app = (BattleshipApplication) activity.getApplication();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        String username = sharedPref.getString("username", null);

        if (username != null) {
            Bitmap image = Utils.getUserImage(activity);

            user = new User(username, image);

            app.setUser(user);
        }

        return user;
    }

    public static void savePreferences(Activity activity) {
        BattleshipApplication app = (BattleshipApplication) activity.getApplication();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", app.getUser().getUsername());
        editor.apply();

        Toast.makeText(activity, "Dados do utilizador guardados.", Toast.LENGTH_SHORT).show();
    }
}
