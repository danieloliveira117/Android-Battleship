package amov.danieloliveira.batalhanaval.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import amov.danieloliveira.batalhanaval.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkHasUserAccount();

        setContentView(R.layout.activity_main);
    }

    public void checkHasUserAccount() {
        String username = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString("username", null);

        if (username == null) {
            startActivity(new Intent(this, FirstRunActivity.class));

            /*getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();*/
        }
    }
}
