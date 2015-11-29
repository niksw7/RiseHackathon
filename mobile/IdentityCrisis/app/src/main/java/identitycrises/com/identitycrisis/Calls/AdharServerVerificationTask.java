package identitycrises.com.identitycrisis.Calls;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import identitycrises.com.identitycrisis.Constants.KeyConstants;
import identitycrises.com.identitycrisis.Interfaces.ServerRequestListener;
import identitycrises.com.identitycrisis.Utils.ConnectionUtils;
import identitycrises.com.identitycrisis.WebEntities.WeAdhaarDetails;

/**
 * Created by Sagar Makwana on 29/11/15.
 */
public class AdharServerVerificationTask extends AsyncTask<WeAdhaarDetails, Void, Integer> {

    private static final String TAG = AdharServerVerificationTask.class.getSimpleName();
    private ServerRequestListener serverRequestListener;
    private HttpURLConnection connection = null;
    private WeAdhaarDetails weAdhaarDetails;


    public AdharServerVerificationTask(ServerRequestListener serverRequestListener){
        this.serverRequestListener = serverRequestListener;
    }
    @Override
    protected Integer doInBackground(WeAdhaarDetails... weAdhaarDetailses) {

        weAdhaarDetails = weAdhaarDetailses[0];
        Gson gson = new Gson();
        int responseCode = 0;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = ConnectionUtils.getPostConnection(KeyConstants.KEY_ROOT_URL + KeyConstants.UPDATE_ADHAR_INFORMATION);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(600000);
            connection.setReadTimeout(600000);
            String jsonString = gson.toJson(weAdhaarDetails);
            Log.e(TAG,jsonString);
            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            writer.println(jsonString);
            writer.flush();
            writer.close();
            responseCode = connection.getResponseCode();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return responseCode;
        }

        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer integer) {

        if (integer == KeyConstants.POST_OK) {
            serverRequestListener.onCallSuccess();
        }
        else {
            serverRequestListener.onCallFailure();
        }

    }
}
