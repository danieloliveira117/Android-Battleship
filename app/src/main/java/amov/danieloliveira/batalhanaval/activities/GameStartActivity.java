package amov.danieloliveira.batalhanaval.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.GameCommunication;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.SerializableHashSet;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.BOT_NAME;
import static amov.danieloliveira.batalhanaval.Consts.CLIENT;
import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;

// TODO Check if PlayerType remains the same when mode changes to single player!!!
// TODO clear game data on back / game end / lost connection / on Pause????
public class GameStartActivity extends AppCompatActivity {
    private static final String TAG = "GameStartActivity";
    public static final String KEY_LAST_ORIENTATION = "last_orientation";
    public static final String KEY_PLACED_VIEWS = "placed_views";

    private GameObservable gameObs;
    private GameCommunication gameCommunication;

    private int mode = SINGLEPLAYER;
    private PlayerType type;
    private TableLayout tbl_place_ships;
    private TableLayout tbl_shipyard;
    private MenuItem menu_action_change_mode;

    public SerializableHashSet<Integer> placedViews = new SerializableHashSet<>();
    private boolean isFirstStart;
    private boolean startingGame;

    @Override
    protected void onStart() {
        super.onStart();

        if (isFirstStart) {
            startNewGame();

            isFirstStart = false;
        }

        startingGame = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Terminar ligação se estiver em multiplayer
        if (!startingGame && !isChangingConfigurations()
                && mode != SINGLEPLAYER && gameCommunication != null) {
            gameCommunication.endCommunication();
            gameCommunication = null;
        }
    }

    private void startNewGame() {
        gameObs.newGameData();
        placedViews.clear();

        if (mode != SINGLEPLAYER) {
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            assert connMgr != null;

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                Toast.makeText(this, R.string.error_netconn, Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            BattleshipApplication app = (BattleshipApplication) getApplication();

            // Start Game Communication
            gameCommunication = app.newGameCommunication(this, mode);
            gameCommunication.startCommunication();
        } else {
            gameObs.setAdversaryUser(new User(BOT_NAME, null));
            gameObs.startGame(GameMode.vsAI, Utils.getUser(this), false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(KEY_PLACED_VIEWS, placedViews);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Avoid warnings (cast generics)
        //placedViews = (SerializableHashSet<Integer>) savedInstanceState.getSerializable("placedViews");
        Object temp = savedInstanceState.getSerializable(KEY_PLACED_VIEWS);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        if (savedInstanceState == null) {
            isFirstStart = true;
        }

        tbl_place_ships = findViewById(R.id.tbl_place_ships);
        tbl_shipyard = findViewById(R.id.tbl_shipyard);

        BattleshipApplication app = (BattleshipApplication) getApplication();

        gameObs = app.getObservable();

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getIntExtra("mode", SINGLEPLAYER);
        }

        if (mode == CLIENT) {
            type = PlayerType.ADVERSARY;
            tbl_place_ships.setTag(1);
            tbl_shipyard.setTag(1);
        } else {
            type = PlayerType.PLAYER;
            tbl_place_ships.setTag(0);
            tbl_shipyard.setTag(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_start_menu, menu);

        menu_action_change_mode = menu.findItem(R.id.action_change_mode);

        if (mode == SINGLEPLAYER) {
            menu_action_change_mode.setVisible(false);
        } else {
            menu_action_change_mode.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_mode: {
                // TODO: 15/08/2018 Change mode to single player
                menu_action_change_mode.setVisible(false);
            }
            break;
            case R.id.action_randomize_placement: {
                hideAllShipsInShipyard();
                gameObs.randomizePlacement(type);
            }
            break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void onConfirmPlacement(View view) {
        gameObs.confirmPlacement(type);

        if (gameObs.validPlacement(type)) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", mode);
            startActivity(intent);

            startingGame = true;

            finish();
        } else {
            Toast.makeText(this, R.string.ships_not_placed_correctly, Toast.LENGTH_LONG).show();
        }
    }

    public void onRotateShip(View view) {
        gameObs.rotateCurrentShip(type);
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
}
