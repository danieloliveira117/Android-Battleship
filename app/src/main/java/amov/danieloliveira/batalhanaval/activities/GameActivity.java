package amov.danieloliveira.batalhanaval.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.GameCommunication;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;

// TODO clear game data on back / game end / lost connection / on Pause????
public class GameActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "GameActivity";
    private GameObservable gameObs;
    private GameCommunication gameCommunication;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getIntExtra("mode", SINGLEPLAYER);
        }

        BattleshipApplication app = (BattleshipApplication) this.getApplication();

        gameCommunication = app.getGameCommunication();

        gameObs = app.getObservable();
        gameObs.addObserver(this);

        updateAdversary(gameObs.getAdversary());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mode != SINGLEPLAYER) {
            finish();
        } else {
            updateAdversary(gameObs.getAdversary());
            // TODO: 15/08/2018 restore game
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
    public void update(Observable o, Object arg) {

    }

    private void updateAdversary(User adversary) {
        // Update Views
        CircularImageView civ_adversary_avatar = findViewById(R.id.civ_adversary_avatar);
        AppCompatTextView tv_adversary_username = findViewById(R.id.tv_adversary_username);

        if (adversary.getImage() == null) {
            civ_adversary_avatar.setImageResource(R.drawable.default_image);
        } else {
            civ_adversary_avatar.setImageBitmap(adversary.getImage());
        }

        tv_adversary_username.setText(adversary.getUsername());
    }
}