package identitycrises.com.identitycrisis.Application;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

/**
 * Created by root on 29/11/15.
 */
public class IdentityCrisisApplication extends Application {

    private static File DIRECTORY_URI;

    public static final String CUSTOMER_ID = "customerId";

    public static SharedPreferences appPrefs;

    public static SharedPreferences.Editor editor;



    @Override
    public void onCreate() {
        super.onCreate();
        DIRECTORY_URI = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        appPrefs = this.getSharedPreferences("APP_Prefs", Activity.MODE_PRIVATE);
        editor = appPrefs.edit();

    }

    public static File getDirectoryUri() {
        return DIRECTORY_URI;
    }

    public static void setDirectoryUri(File directoryUri) {
        DIRECTORY_URI = directoryUri;
    }

}
