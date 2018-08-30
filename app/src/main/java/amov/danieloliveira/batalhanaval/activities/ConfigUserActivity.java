package amov.danieloliveira.batalhanaval.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import amov.danieloliveira.batalhanaval.BattleshipApplication;
import amov.danieloliveira.batalhanaval.Preferences;
import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class ConfigUserActivity extends AppCompatActivity {
    private static final String TAG = "ConfigUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_user);

        AppCompatImageView imageView = findViewById(R.id.user_image);
        Bitmap image = Utils.getUserImage(this);

        if (image != null) {
            imageView.setImageBitmap(image);
        }

        User user = Utils.getUser(this);

        if(user != null) {
            // show back button
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);

            TextInputEditText text_input = findViewById(R.id.edit_username);
            text_input.setText(user.getUsername());
            text_input.setSelection(text_input.getText().length()); // set the cursor to the end of the EditText
        }
    }

    public void onCreateUser(View view) {
        TextInputEditText editUsername = findViewById(R.id.edit_username);
        String username = editUsername.getText().toString().trim();

        if(username.length() < 3) {
            Toast.makeText(this, getResources().getString(R.string.username_min_length), Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap image = Utils.getUserImage(this);

        if(image == null) {
            Toast.makeText(this, getResources().getString(R.string.image_missing), Toast.LENGTH_SHORT).show();
            return;
        }

        BattleshipApplication app = (BattleshipApplication) this.getApplication();
        app.setUser(new User(username, image));
        Preferences.saveUserPreferences(this);

        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    public void onEditImage(View view) {
        Intent intent = new Intent(this, TakePictureActivity.class);
        startActivityForResult(intent, 413);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 413) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "RESULT_OK");

                // Imagem encontrada a modificar a imagem do utilizador
                AppCompatImageView imageView = findViewById(R.id.user_image);
                Bitmap image = Utils.getUserImage(this);

                if (image != null) {
                    imageView.setImageBitmap(image);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.error_image), Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "RESULT_CANCELED");
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
