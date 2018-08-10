package amov.danieloliveira.batalhanaval.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import amov.danieloliveira.batalhanaval.Preferences;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.CLIENT;
import static amov.danieloliveira.batalhanaval.Consts.SERVER;
import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = Preferences.loadPreferences(MainActivity.this);

        if(user == null) {
            // Primeira utilização necessita de configurar o utilizador
            startActivity(new Intent(this, ConfigUserActivity.class));
            finish();
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    public void onOnePlayer(View view) {
        Intent intent = new Intent(this, GameStartActivity.class);

        intent.putExtra("mode", SINGLEPLAYER);

        startActivity(intent);
    }

    public void onCreateServer(View view) {
        Intent intent = new Intent(this, GameStartActivity.class);

        intent.putExtra("mode", SERVER);

        startActivity(intent);
    }

    public void onJoinServer(View view) {
        Intent intent = new Intent(this, GameStartActivity.class);

        intent.putExtra("mode", CLIENT);

        startActivity(intent);
    }

    public void onMatchHistory(View view) {

    }

    public void onChangeUser(View view) {
        startActivity(new Intent(this, ConfigUserActivity.class));
    }


}
