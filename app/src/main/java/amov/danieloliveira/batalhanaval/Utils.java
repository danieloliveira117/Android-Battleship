package amov.danieloliveira.batalhanaval;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.model.User;

import static amov.danieloliveira.batalhanaval.Consts.IMAGE_NAME;

public class Utils {
    public static GameObservable getObservable(Context context) {
        BattleshipApplication app = (BattleshipApplication) ((Activity) context).getApplication();

        return app.getObservable();
    }

    public static GameObservable getObservable(Activity activity) {
        BattleshipApplication app = (BattleshipApplication) activity.getApplication();

        return app.getObservable();
    }

    public static User getUser(Activity activity) {
        BattleshipApplication app = (BattleshipApplication) activity.getApplication();

        return app.getUser();
    }

    public static Bitmap getUserImage() {
        File file = new File(Environment.getExternalStorageDirectory() + IMAGE_NAME);
        Bitmap image = null;

        if (file.exists()) {
            image = BitmapFactory.decodeFile(file.getAbsolutePath());
        }

        return image;
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();

            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();

                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();

                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /* thanks to: http://mobile.cs.fsu.edu/converting-images-to-json-objects/ */

    /**
     * This functions converts Bitmap picture to a string which can be JSONified.
     * */
    public static String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;

        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY, byteArrayBitmapStream);

        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedImage;
    }

    /**
     * This Function converts the String back to Bitmap
     * */
    public static Bitmap getBitmapFromString(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
