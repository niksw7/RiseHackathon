package identitycrises.com.identitycrisis.Calls;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import identitycrises.com.identitycrisis.Constants.KeyConstants;
import identitycrises.com.identitycrisis.Interfaces.ServerRequestListener;
import identitycrises.com.identitycrisis.UI.RegisterActivity;
import identitycrises.com.identitycrisis.Utils.ConnectionUtils;
import identitycrises.com.identitycrisis.WebEntities.WeRegistrationDetails;
import identitycrises.com.identitycrisis.WebEntities.WeRegistrationResponse;

/**
 * Created by Sagar Makwana on 29/11/15.
 */
public class RegistrationTask extends AsyncTask<WeRegistrationDetails,Void,Integer> {

    private static final String TAG = RegistrationTask.class.getSimpleName();

    private ServerRequestListener serverRequestListener;

    private HttpURLConnection connection = null;

    private WeRegistrationDetails weRegistrationDetails;

    private WeRegistrationResponse weRegistrationResponse;

    public RegistrationTask(ServerRequestListener serverRequestListener){
        this.serverRequestListener = serverRequestListener;
    }
    @Override
    protected Integer doInBackground(WeRegistrationDetails... weRegistrationDetailses) {
        weRegistrationDetails = weRegistrationDetailses[0];

        Gson gson = new Gson();
        int responseCode = 0;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = ConnectionUtils.getPostConnection(KeyConstants.KEY_ROOT_URL + KeyConstants.ADD_CUSTOMER);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(60000);
            connection.setReadTimeout(60000);
            String jsonString = gson.toJson(weRegistrationDetails);
            Log.e(TAG, jsonString);
            PrintWriter writer = new PrintWriter(connection.getOutputStream());
            writer.println(jsonString);
            writer.flush();
            writer.close();
            responseCode = connection.getResponseCode();

            /*if (responseCode == KeyConstants.POST_OK)
            {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                StringBuffer buffer = new StringBuffer();
                while (line != null)
                {
                    buffer.append(line);
                    line = reader.readLine();
                }
                weRegistrationResponse = gson.fromJson(buffer.toString(), new TypeToken<WeRegistrationResponse>() {}.getType());

            }*/


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
            //((RegisterActivity)serverRequestListener).storeCustomerId(weRegistrationResponse);
            serverRequestListener.onCallSuccess();
        }
        else {
            serverRequestListener.onCallFailure();
        }
    }
}
