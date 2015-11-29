package identitycrises.com.identitycrisis.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import identitycrises.com.identitycrisis.Application.IdentityCrisisApplication;
import identitycrises.com.identitycrisis.Calls.AdharServerVerificationTask;
import identitycrises.com.identitycrisis.Constants.KeyConstants;
import identitycrises.com.identitycrisis.Interfaces.ServerRequestListener;
import identitycrises.com.identitycrisis.R;
import identitycrises.com.identitycrisis.Utils.ConnectionUtils;
import identitycrises.com.identitycrisis.WebEntities.WeAdhaarDetails;

public class AdhaarVerificationActivity extends AppCompatActivity {

    private WeAdhaarDetails weAdhaarDetails;

    private ProgressDialog progressDialog;

    private EditText uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhaar_verification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.ic_app);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating Adhaar Details..");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        weAdhaarDetails = new WeAdhaarDetails();
        weAdhaarDetails.setBioKey(KeyConstants.BIO_KEY);
        weAdhaarDetails.setCustomerId("1");
        //weAdhaarDetails.setCustomerId(IdentityCrisisApplication.appPrefs.getString(IdentityCrisisApplication.CUSTOMER_ID,"1"));


        uid = (EditText)findViewById(R.id.edt_uid);

        ImageView fingerPrint = (ImageView)findViewById(R.id.fingerPrint);
        fingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionUtils.isNetworkAvailable(AdhaarVerificationActivity.this)){

                    progressDialog.show();

                    if( uid.getText()!= null ){
                        weAdhaarDetails.setAdharNumber(uid.getText().toString());
                    }

                    new AdharServerVerificationTask(new ServerRequestListener() {
                        @Override
                        public void onCallSuccess() {

                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent(AdhaarVerificationActivity.this,EMudraVerificationActivity.class);
                            startActivity(intent);
                            Toast.makeText(AdhaarVerificationActivity.this, "Authentication Successful..", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCallFailure() {
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }

                                Toast.makeText(AdhaarVerificationActivity.this,"Authentication Failed.Try Again..",Toast.LENGTH_SHORT).show();

                        }
                    }).execute(weAdhaarDetails);
                }
                else {

                    Toast.makeText(AdhaarVerificationActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adhaar_verification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
