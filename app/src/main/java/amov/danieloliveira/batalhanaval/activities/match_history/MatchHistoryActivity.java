package amov.danieloliveira.batalhanaval.activities.match_history;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.R;

public class MatchHistoryActivity extends AppCompatActivity {
    BattleshipApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);

        app = (BattleshipApplication) getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.match_history_menu, menu);

        return true;
    }

    public void onClearMatchHistory(MenuItem item) {
        closeOptionsMenu();
        app.clearMatchHistory();
        recreate();
    }
}
