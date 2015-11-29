package identitycrises.com.identitycrisis.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import identitycrises.com.identitycrisis.Application.IdentityCrisisApplication;
import identitycrises.com.identitycrisis.Calls.RegistrationTask;
import identitycrises.com.identitycrisis.Interfaces.ServerRequestListener;
import identitycrises.com.identitycrisis.R;
import identitycrises.com.identitycrisis.Utils.ConnectionUtils;
import identitycrises.com.identitycrisis.WebEntities.WeRegistrationDetails;
import identitycrises.com.identitycrisis.WebEntities.WeRegistrationResponse;


public class RegisterActivity extends AppCompatActivity implements ServerRequestListener {

    private EditText edtName, edtAddress, edtPassword , edtUsername, edtAge;

    private Button btnSubmit;

    private ProgressDialog progressDialog;

    private WeRegistrationDetails weRegistrationDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.ic_app);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Authenticating Details..");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        weRegistrationDetails = new WeRegistrationDetails();

        edtName = (EditText) findViewById(R.id.edt_name);
        edtUsername = (EditText) findViewById(R.id.edt_usrname);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        edtAge = (EditText) findViewById(R.id.edt_age);

        TextView click = (TextView)findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,DocumentSelectActivity.class);
                startActivity(intent);
            }
        });


        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ConnectionUtils.isNetworkAvailable(RegisterActivity.this)){

                    progressDialog.show();

                    if (edtUsername.getText() != null && edtUsername.getText().toString()!= ""){
                        weRegistrationDetails.setUserName(edtUsername.getText().toString());
                    }

                    if (edtPassword.getText() != null && edtPassword.getText().toString()!= ""){
                        weRegistrationDetails.setPassword(edtPassword.getText().toString());
                    }

                    if (edtName.getText() != null && edtName.getText().toString()!= ""){
                        weRegistrationDetails.setName(edtName.getText().toString());
                    }

                    if (edtAddress.getText() != null && edtAddress.getText().toString()!= ""){
                        weRegistrationDetails.setAddress(edtAddress.getText().toString());
                    }

                    if (edtAge.getText() != null && edtAge.getText().toString()!= ""){
                        weRegistrationDetails.setAge(edtAge.getText().toString());
                    }
                }

                new RegistrationTask(RegisterActivity.this).execute(weRegistrationDetails);

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

    @Override
    public void onCallSuccess() {

        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        Toast.makeText(RegisterActivity.this, "Authentication Successful..", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterActivity.this,DocumentSelectActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onCallFailure() {

        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        Toast.makeText(RegisterActivity.this, "Authentication Failed.Try Again..", Toast.LENGTH_SHORT).show();

    }

    public void storeCustomerId(WeRegistrationResponse weRegistrationResponse){
        IdentityCrisisApplication.editor.putString(IdentityCrisisApplication.CUSTOMER_ID , weRegistrationResponse.getCustomerId()).commit();
        Log.e("Cid",weRegistrationResponse.getCustomerId());
    }
}
