package amov.danieloliveira.batalhanaval.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import amov.danieloliveira.batalhanaval.Preferences;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = Preferences.loadPreferences(MainActivity.this);

        if(user == null) {
            // Primeira utilização necessita de configurar o utilizador
            startActivity(new Intent(this, ConfigUserActivity.class));
            this.finish();
        } else {
            setContentView(R.layout.activity_main);
        }
    }
}
