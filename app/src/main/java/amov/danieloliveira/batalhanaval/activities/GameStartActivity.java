package amov.danieloliveira.batalhanaval.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.GameCommunication;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.CLIENT;
import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;

// TODO clear game data on back / game end / lost connection / on Pause????
public class GameStartActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "GameStartActivity";
    private GameObservable gameObs;
    private GameCommunication gameCommunication;
    public Set<Integer> placedViews = new HashSet<>();

    private int mode = SINGLEPLAYER;
    private PlayerType player;

    @Override
    protected void onResume() {
        super.onResume();

        if (mode != SINGLEPLAYER) {
            gameObs.newGameData();
            BattleshipApplication app = (BattleshipApplication) this.getApplication();
            gameCommunication = app.newGameCommunication(this, mode);
        } else {
            gameObs.setAdversaryUser(new User("BotTron2000", null));
            gameObs.startGame(GameMode.vsAI, Utils.getUser(this));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (gameCommunication != null) {
            gameCommunication.endCommunication();
            gameCommunication = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        BattleshipApplication app = (BattleshipApplication) this.getApplication();

        gameObs = app.getObservable();
        gameObs.addObserver(this);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connMgr != null;

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this, R.string.error_netconn, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getIntExtra("mode", SINGLEPLAYER);
        }

        if (mode == CLIENT) {
            player = PlayerType.ADVERSARY;
        } else {
            player = PlayerType.PLAYER;
        }

        // Start Game Communication
        gameObs.newGameData();
        gameCommunication = app.newGameCommunication(this, mode);
        gameCommunication.startCommunication();
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    public void onConfirmPlacement(View view) {
        gameObs.confirmPlacement(player);

        if (gameObs.validPlacement(player)) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", mode);
            startActivity(intent);

            finish();
        } else {
            Toast.makeText(this, R.string.ships_not_placed_correctly, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_start_menu, menu);

        if (mode == SINGLEPLAYER) {
            menu.findItem(R.id.action_change_mode).setVisible(true);
        } else {
            menu.findItem(R.id.action_change_mode).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_mode:

                // TODO: 15/08/2018 Change mode to single player
                MenuItem changeMode = findViewById(R.id.action_change_mode);
                changeMode.setVisible(false);
                return true;

            case R.id.action_randomize_placement:
                hideAllShipsInShipyard();

                gameObs.randomizePlacement(player);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void hideAllShipsInShipyard() {
        TableLayout layout = findViewById(R.id.tbl_shipyard);

        if (layout != null) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                TableRow row = (TableRow) layout.getChildAt(i);

                for (int j = 0; j < row.getChildCount(); j++) {
                    row.getChildAt(j).setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
