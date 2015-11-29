package identitycrises.com.identitycrisis.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Connection utils class which generate connection for different method types.
 * Created by Neebal technologies on 13/9/15.
 */
public class ConnectionUtils {

    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final String PUT_METHOD = "PUT";

    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String JSON_CONTENT_TYPE_VALUE = "application/json";

    public static HttpURLConnection getGetConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(GET_METHOD);
        connection.setDoInput(true);
        connection.setRequestProperty(CONTENT_TYPE_KEY, JSON_CONTENT_TYPE_VALUE);
        return connection;
    }

    public static HttpURLConnection getPostConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(POST_METHOD);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty(CONTENT_TYPE_KEY, JSON_CONTENT_TYPE_VALUE);
        return connection;
    }

    public static HttpURLConnection getPutConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(PUT_METHOD);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        return connection;
    }

    /**
     * Method to check if user is connected to Internet or not.
     * @param context : context for getting system service.
     * @return true if user is connected to Internet else false.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
