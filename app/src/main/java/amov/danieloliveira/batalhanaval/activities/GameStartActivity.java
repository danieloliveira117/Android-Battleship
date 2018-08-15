package amov.danieloliveira.batalhanaval.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.GameCommunication;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;

// TODO clear game data on back / game end / lost connection / on Pause????
// TODO: 14/08/2018 REPLACE ALL PLAYERTYPEs
public class GameStartActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "GameStartActivity";
    private GameObservable gameObs;
    private GameCommunication gameCommunication;
    public Set<Integer> placedViews = new HashSet<>();

    private int mode = SINGLEPLAYER;

    @Override
    protected void onResume() {
        super.onResume();

        if (mode != SINGLEPLAYER) {
            BattleshipApplication app = (BattleshipApplication) this.getApplication();
            gameCommunication = app.newGameCommunication(this, mode);
        } else {
            gameObs.setAdversary(new User("BotTron2000", null));
            gameObs.startGame(GameMode.vsAI, null);
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

        // Start Game Communication
        gameCommunication = app.newGameCommunication(this, mode);
        gameCommunication.startCommunication();
    }

    @Override
    public void update(Observable o, Object arg) {
    }

    public void onConfirmPlacement(View view) {
        gameObs.confirmPlacement(PlayerType.PLAYER);

        if(gameObs.validPlacement(PlayerType.PLAYER)) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("mode", mode);
            startActivity(intent);

            finish();
        } else {
            Toast.makeText(this, R.string.ships_not_placed_correctly, Toast.LENGTH_LONG).show();
        }
    }
}
