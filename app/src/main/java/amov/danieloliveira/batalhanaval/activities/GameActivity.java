package amov.danieloliveira.batalhanaval.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.GameCommunication;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.engine.BotThread;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Board;
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
    private PlayerType opponent;
    private BotThread botThread;
    private Handler handler;
    private ProgressDialog pd;

    private boolean onCreateRunned = false;

    private RelativeLayout rel_adversary_ships;
    private ConstraintLayout rel_reposition_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        onCreateRunned = true;

        rel_adversary_ships = findViewById(R.id.relative_adversary_ships);
        rel_reposition_buttons = findViewById(R.id.rel_reposition_buttons);

        TableLayout tbl_adversary_ships = findViewById(R.id.tbl_adversary_ships);
        TableLayout tbl_player_ships = findViewById(R.id.tbl_player_ships);

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getIntExtra("mode", SINGLEPLAYER);
        }

        BattleshipApplication app = (BattleshipApplication) this.getApplication();
        gameCommunication = app.getGameCommunication();

        gameObs = app.getObservable();
        gameObs.addObserver(this);

        if (mode == CLIENT) {
            opponent = PlayerType.PLAYER;
            player = PlayerType.ADVERSARY;
            tbl_player_ships.setTag(1);
            tbl_adversary_ships.setTag(0);
        } else {
            opponent = PlayerType.ADVERSARY;
            player = PlayerType.PLAYER;
            tbl_player_ships.setTag(0);
            tbl_adversary_ships.setTag(1);
        }

        if (!gameObs.validPlacement(opponent)) {
            pd = new ProgressDialog(this);
            pd.setMessage(this.getString(R.string.waiting_for_opponent_msg));
            pd.setTitle(R.string.app_name);

            pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });

            pd.show();
            return;
        }

        prepareBoard();

        if (mode == SINGLEPLAYER) {
            startBot();
        }
    }

    private void startBot() {
        handler = new Handler();
        botThread = new BotThread(handler, gameObs);
        botThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!onCreateRunned) {
            if (mode != SINGLEPLAYER) {
                finish();
            } else {
                // TODO: 15/08/2018 restore game ??
                prepareBoard();
                handler = new Handler();
                botThread = new BotThread(handler, gameObs);
                botThread.start();
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

        if (botThread != null) {
            botThread.interrupt();
            botThread = null;
        }
    }

    private void prepareBoard() {
        updatePlayer(gameObs.getPlayerUser(PlayerType.PLAYER));
        updateAdversary(gameObs.getPlayerUser(PlayerType.ADVERSARY));

        gameObs.refreshData();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Board) {
            if (gameObs.validPlacement(opponent) && pd != null) {
                pd.dismiss();
                prepareBoard();
            }
        } else {
            setCurrentPlayerTurn(gameObs.getCurrentPlayer());

            if (gameObs.didGameEnd()) {
                AppCompatTextView end_game_message = findViewById(R.id.end_game_message);

                end_game_message.setText(String.format(getResources().getString(R.string.end_game_message), gameObs.getCurrentUser().getUsername()));
                end_game_message.setVisibility(View.VISIBLE);

                gameObs.deleteObserver(this);
            } else if (gameObs.isShipReposition(player)) {
                if (rel_adversary_ships.getVisibility() != View.GONE) {
                    rel_adversary_ships.setVisibility(View.GONE);
                    rel_reposition_buttons.setVisibility(View.VISIBLE);
                }
            }
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

        current.setBackgroundResource(R.color.miss);
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

    public void onConfirmPlacement(View view) {
        gameObs.confirmPlacement(player);

        if (gameObs.validPlacement(player)) {
            rel_adversary_ships.setVisibility(View.VISIBLE);
            rel_reposition_buttons.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, R.string.ships_not_placed_correctly, Toast.LENGTH_LONG).show();
        }
    }

    public void onRotateShip(View view) {
        gameObs.rotateCurrentShip(player);
    }

    public void onRestorePosition(View view) {
        gameObs.restorePosition(player);
    }
}
