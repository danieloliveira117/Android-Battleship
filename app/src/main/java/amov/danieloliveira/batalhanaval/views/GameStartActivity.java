package amov.danieloliveira.batalhanaval.views;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import amov.danieloliveira.batalhanaval.GameCommunication;
import amov.danieloliveira.batalhanaval.R;

import static amov.danieloliveira.batalhanaval.Consts.SINGLEPLAYER;

public class GameStartActivity extends AppCompatActivity {
    private static final String TAG = "GameStartActivity";

    private int mode = SINGLEPLAYER;
    private Handler procMsg = null;
    private GameCommunication gameCommunication;

    @Override
    protected void onResume() {
        super.onResume();

        if(mode != SINGLEPLAYER) {
            gameCommunication = new GameCommunication(this, procMsg, mode);
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
    }
}
