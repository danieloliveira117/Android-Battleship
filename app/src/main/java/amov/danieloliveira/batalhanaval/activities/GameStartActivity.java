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

import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.GameCommunication;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.SerializableHashSet;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.User;
import amov.danieloliveira.batalhanaval.views.BattleshipBoard;

import static amov.danieloliveira.batalhanaval.Consts.CLIENT;
import static amov.danieloliveira.batalhanaval.Consts.SERVER;
import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;

// TODO: 19/08/2018 Check if PlayerType remains the same when mode changes to single player!!!
// TODO clear game data on back / game end / lost connection / on Pause????
public class GameStartActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "GameStartActivity";
    private GameObservable gameObs;
    private GameCommunication gameCommunication;

    private int mode = SINGLEPLAYER;
    private PlayerType type;
    private BattleshipBoard tbl_place_ships;

    public SerializableHashSet<Integer> placedViews = new SerializableHashSet<>();

    @Override
    protected void onResume() {
        super.onResume();

        /*if (mode != SINGLEPLAYER) {
            gameObs.newGameData();
            BattleshipApplication app = (BattleshipApplication) this.getApplication();
            gameCommunication = app.newGameCommunication(this, mode);
        }*/
        // TODO: 20/08/2018 ???
    }

    @Override
    protected void onPause() {
        super.onPause();

        // TODO: 20/08/2018 End if not rotation
        /*if (gameCommunication != null) {
            gameCommunication.endCommunication();
            gameCommunication = null;
        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("placedViews", placedViews);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        tbl_place_ships = findViewById(R.id.tbl_place_ships);

        BattleshipApplication app = (BattleshipApplication) this.getApplication();

        gameObs = app.getObservable();
        gameObs.addObserver(this);

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getIntExtra("mode", SINGLEPLAYER);
        }

        if (mode == CLIENT) {
            type = PlayerType.ADVERSARY;
            tbl_place_ships.setTag(1);
        } else {
            type = PlayerType.PLAYER;
            tbl_place_ships.setTag(0);
        }

        if (savedInstanceState == null || savedInstanceState.getSerializable("placedViews") == null) {
            // Start Game Communication
            gameObs.newGameData();

            if (mode != SINGLEPLAYER) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                assert connMgr != null;

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected()) {
                    Toast.makeText(this, R.string.error_netconn, Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

                gameCommunication = app.newGameCommunication(this, mode);
                gameCommunication.startCommunication();
            } else {
                gameObs.setAdversaryUser(new User("BotTron2000", null));
                gameObs.startGame(GameMode.vsAI, Utils.getUser(this), false);
            }
        } else {
            // Avoid warnings (cast generics)
            //placedViews = (SerializableHashSet<Integer>) savedInstanceState.getSerializable("placedViews");
            Object temp = savedInstanceState.getSerializable("placedViews");

            placedViews.clear();

            if (temp instanceof SerializableHashSet) {
                SerializableHashSet temp2 = (SerializableHashSet) temp;

                for (Object o : temp2) {
                    if (o instanceof Integer) {
                        placedViews.add((Integer) o);
                    }
                }
            }
            // end

            hidePlacedShipsInShipyard();
            gameObs.refreshData();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void onConfirmPlacement(View view) {
        gameObs.confirmPlacement(type);

        if (gameObs.validPlacement(type)) {
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
            menu.findItem(R.id.action_change_mode).setVisible(false);
        } else {
            menu.findItem(R.id.action_change_mode).setVisible(true);
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

                gameObs.randomizePlacement(type);
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

    private void hidePlacedShipsInShipyard() {
        TableLayout layout = findViewById(R.id.tbl_shipyard);

        if (layout != null) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                TableRow row = (TableRow) layout.getChildAt(i);

                for (int j = 0; j < row.getChildCount(); j++) {
                    View view = row.getChildAt(j);

                    if (placedViews.contains((Integer) view.getTag())) {
                        view.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    public void onRotateShip(View view) {
        gameObs.rotateCurrentShip(type);
    }
}
