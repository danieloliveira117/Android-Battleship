package amov.danieloliveira.batalhanaval.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import static amov.danieloliveira.batalhanaval.Consts.BOT_NAME;
import static amov.danieloliveira.batalhanaval.Consts.CLIENT;
import static amov.danieloliveira.batalhanaval.Consts.MAXSELECT;
import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;

public class GameActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "GameActivity";
    private GameObservable gameObs;
    private GameCommunication gameCommunication;
    private BotThread botThread;

    private int mode;
    private PlayerType player;
    private PlayerType opponent;
    private ProgressDialog pd;

    private boolean onCreateHasRun = false;

    private RelativeLayout rel_adversary_ships;
    private ConstraintLayout rel_reposition_buttons;
    private AppCompatTextView tv_reposition_msg;

    private TableLayout tbl_adversary_ships;
    private TableLayout tbl_player_ships;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        onCreateHasRun = true;

        rel_adversary_ships = findViewById(R.id.relative_adversary_ships);
        rel_reposition_buttons = findViewById(R.id.rel_reposition_buttons);
        tv_reposition_msg = findViewById(R.id.tv_reposition_msg);

        tbl_adversary_ships = findViewById(R.id.tbl_adversary_ships);
        tbl_player_ships = findViewById(R.id.tbl_player_ships);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", SINGLEPLAYER);

        BattleshipApplication app = (BattleshipApplication) this.getApplication();
        gameCommunication = app.getGameCommunication();
        if (gameCommunication != null) {
            gameCommunication.setActivity(this);
        }

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
        botThread = new BotThread(gameObs, opponent);
        botThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!onCreateHasRun) {
            if (mode != SINGLEPLAYER) {
                finish();
            } else {
                prepareBoard();
                startBot();
            }
        }

        onCreateHasRun = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (botThread != null) {
            botThread.terminateThread();
            botThread = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Terminar ligação se estiver em multiplayer
        if (!isChangingConfigurations()
                && mode != SINGLEPLAYER
                && gameCommunication != null) {
            gameCommunication.endCommunication();
            gameCommunication = null;
        }
    }

    private void prepareBoard() {
        updatePlayer(gameObs.getPlayerUser(PlayerType.PLAYER));
        updateAdversary(gameObs.getPlayerUser(PlayerType.ADVERSARY));

        rel_adversary_ships.setVisibility(View.VISIBLE);
        rel_reposition_buttons.setVisibility(View.GONE);
        tv_reposition_msg.setVisibility(View.GONE);

        gameObs.refreshData();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Board) {
            if (gameObs.validPlacement(opponent)) {
                if (pd != null) {
                    pd.dismiss();
                }

                prepareBoard();
            }
        } else if (arg == BOT_NAME) {
            mode = SINGLEPLAYER;
            gameObs.setUser(opponent, new User(BOT_NAME, null));
            prepareBoard();
            startBot();
        }

        setCurrentPlayerTurn(gameObs.getCurrentPlayer());

        if (gameObs.didGameEnd()) {
            AppCompatTextView end_game_message = findViewById(R.id.end_game_message);

            end_game_message.setText(String.format(getResources().getString(R.string.end_game_message), gameObs.getCurrentUser().getUsername()));
            end_game_message.setVisibility(View.VISIBLE);

            gameObs.deleteObserver(this);
        } else if (rel_adversary_ships.getVisibility() == View.VISIBLE) {
            if (gameObs.isShipReposition(player)) {
                Toast.makeText(this, String.format(getString(R.string.player_reposition_msg), MAXSELECT), Toast.LENGTH_LONG).show();
                rel_adversary_ships.setVisibility(View.GONE);
                rel_reposition_buttons.setVisibility(View.VISIBLE);
            } else if (gameObs.isShipReposition(opponent)) {
                Toast.makeText(this, String.format(getString(R.string.opponent_reposition_msg), MAXSELECT), Toast.LENGTH_LONG).show();
                rel_adversary_ships.setVisibility(View.INVISIBLE);
                tv_reposition_msg.setVisibility(View.VISIBLE);
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

    public void onChangeToSinglePlayer(MenuItem item) {
        if (!gameObs.didGameEnd()) {
            gameCommunication.endCommunication();
        }
    }
}
