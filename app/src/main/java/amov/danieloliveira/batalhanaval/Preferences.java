package amov.danieloliveira.batalhanaval;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import amov.danieloliveira.batalhanaval.engine.JsonMessage;
import amov.danieloliveira.batalhanaval.engine.model.MatchHistory;
import amov.danieloliveira.batalhanaval.engine.model.User;
import amov.danieloliveira.batalhanaval.engine.model.UserBase64;

public class Preferences {
    public static User loadUserPreferences(Activity activity) {
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

    public static void saveUserPreferences(Activity activity) {
        BattleshipApplication app = (BattleshipApplication) activity.getApplication();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", app.getUser().getUsername());
        editor.apply();

        Toast.makeText(activity, "Dados do utilizador guardados.", Toast.LENGTH_SHORT).show();
    }

    public static void saveMatchHistoryListPreferences(BattleshipApplication app) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(app);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();

        final Type type = new TypeToken<List<MatchHistory>>() {}.getType();

        String json = gson.toJson(app.getMatchHistory(), type);
        editor.putString("MatchHistory", json);
        editor.apply();
    }

    public static void loadMatchHistoryListPreferences(Activity activity) {
        BattleshipApplication app = (BattleshipApplication) activity.getApplication();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(app);
        Gson gson = new Gson();

        final Type type = new TypeToken<List<MatchHistory>>() {}.getType();

        String json = sharedPref.getString("MatchHistory", "");
        List<MatchHistory> list = gson.fromJson(json, type);

        if(list != null) {
            app.setMatchHistoryList(list);
        }
    }
}
