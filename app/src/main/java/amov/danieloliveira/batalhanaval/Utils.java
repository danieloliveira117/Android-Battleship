package amov.danieloliveira.batalhanaval;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;

import static amov.danieloliveira.batalhanaval.Preferences.IMAGE_NAME;

public class Utils {
    public static Bitmap GetUserImage() {
        File file = new File(Environment.getExternalStorageDirectory() + IMAGE_NAME);
        Bitmap image = null;

        if (file.exists()) {
            image = BitmapFactory.decodeFile(file.getAbsolutePath());
        }

        return image;
    }
}
