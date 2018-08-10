package amov.danieloliveira.batalhanaval.views;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.GameCommunication;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;

public class GameStartActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "GameStartActivity";
    private GameObservable gameObs;

    private int mode = SINGLEPLAYER;
    private Handler procMsg = null;
    private GameCommunication gameCommunication;

    @Override
    protected void onResume() {
        super.onResume();

        if (mode != SINGLEPLAYER) {
            gameCommunication = new GameCommunication(this, procMsg, mode);
        } else {
            gameObs.setAdversary(new User("BotTron2000", null));
            gameObs.startGame(GameMode.vsAI, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameCommunication.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

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

        procMsg = new Handler();

        gameObs = Utils.getObservable(this);
        gameObs.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        // Atualizar imagem / nome do adversário
        if (arg instanceof User) {
            updateAdversary((User) arg);
        }
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
