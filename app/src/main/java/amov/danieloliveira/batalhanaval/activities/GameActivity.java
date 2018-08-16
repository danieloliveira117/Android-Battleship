package amov.danieloliveira.batalhanaval.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.GameCommunication;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.BotThread;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.CLIENT;
import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;

// TODO clear game data on back / game end / lost connection / on Pause????
public class GameActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "GameActivity";
    private GameObservable gameObs;
    private GameCommunication gameCommunication;
    private int mode;
    private PlayerType player;
    private BotThread botThread;
    private Handler handler;

    private boolean onCreateRunned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        onCreateRunned = true;

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getIntExtra("mode", SINGLEPLAYER);
        }

        if (mode == CLIENT) {
            player = PlayerType.ADVERSARY;
        } else {
            player = PlayerType.PLAYER;
        }

        BattleshipApplication app = (BattleshipApplication) this.getApplication();

        gameCommunication = app.getGameCommunication();

        gameObs = app.getObservable();
        gameObs.addObserver(this);

        updatePlayer(app.getUser());
        updateAdversary(gameObs.getAdversary());

        handler = new Handler();
        botThread = new BotThread(handler, gameObs);
        botThread.start();

        gameObs.refreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!onCreateRunned) {
            if (mode != SINGLEPLAYER) {
                finish();
            } else {
                updateAdversary(gameObs.getAdversary());
                updatePlayer(Utils.getUser(this));
                // TODO: 15/08/2018 restore game
                gameObs.refreshData();
            }
        }

        onCreateRunned = false;
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
        setCurrentPlayerTurn(gameObs.getCurrentPlayer());

        if(gameObs.didGameEnd()) {
            AppCompatTextView end_game_message = findViewById(R.id.end_game_message);

            end_game_message.setText(String.format(getResources().getString(R.string.end_game_message), gameObs.getCurrentUser().getUsername()));
            end_game_message.setVisibility(View.VISIBLE);
        }
    }

    private void setCurrentPlayerTurn(PlayerType player) {
        ConstraintLayout current;
        ConstraintLayout other;

        if (player == PlayerType.PLAYER) {
            current = findViewById(R.id.info_player);
            other = findViewById(R.id.info_adversary);
        } else {
            current = findViewById(R.id.info_adversary);
            other = findViewById(R.id.info_player);
        }

        current.setBackgroundResource(R.color.MISS);
        other.setBackgroundResource(R.color.colorPrimaryDark);
    }

    private void updateAdversary(User adversary) {
        // Update Views
        CircularImageView civ_adversary_avatar = findViewById(R.id.civ_adversary_avatar);
        AppCompatTextView tv_adversary_username = findViewById(R.id.tv_adversary_name);

        if (adversary.getImage() == null) {
            civ_adversary_avatar.setImageResource(R.drawable.default_image);
        } else {
            civ_adversary_avatar.setImageBitmap(adversary.getImage());
        }

        tv_adversary_username.setText(adversary.getUsername());
    }

    private void updatePlayer(User player) {
        // Update Views
        CircularImageView civ_player_avatar = findViewById(R.id.civ_player_avatar);
        AppCompatTextView tv_player_username = findViewById(R.id.tv_player_name);

        if (player.getImage() == null) {
            civ_player_avatar.setImageResource(R.drawable.default_image);
        } else {
            civ_player_avatar.setImageBitmap(player.getImage());
        }

        tv_player_username.setText(player.getUsername());
    }
}
