package identitycrises.com.identitycrisis.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import identitycrises.com.identitycrisis.R;

public class DocumentSelectActivity extends AppCompatActivity {
    RadioButton rdbtnDriverLIcence;
    RadioButton rdbtnPassport;
    RadioButton rdbtnVoterId;
    RadioButton rdbtnAadhar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.ic_app);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        rdbtnAadhar = (RadioButton) findViewById(R.id.rdbtn_aadhar);
        rdbtnDriverLIcence = (RadioButton)findViewById(R.id.rdbtn_driver_licence);
        rdbtnPassport = (RadioButton) findViewById(R.id.rdbtn_passport);
        rdbtnVoterId = (RadioButton) findViewById(R.id.rdbtn_voterid);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumentSelectActivity.this, OtherActivity.class);
                if ((view.getTag() + "").compareTo("passport") == 0) {
                    intent.putExtra("flag", 1);

                } else if ((view.getTag() + "").compareTo("driverlicence") == 0) {
                    intent.putExtra("flag", 2);
                } else {
                    intent.putExtra("flag", 3);
                }
                startActivity(intent);

            }
        };
        rdbtnAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DocumentSelectActivity.this, AdhaarVerificationActivity.class);
                startActivity(intent);
            }
        });

        rdbtnVoterId.setOnClickListener(listener);
        rdbtnDriverLIcence.setOnClickListener(listener);
        rdbtnPassport.setOnClickListener(listener);
    }

    public void proceedToOther(View view) {
        Intent intent;
        if ((view.getTag() + "").compareTo("passport") == 0) {
            intent = new Intent(DocumentSelectActivity.this, OtherActivity.class);
            intent.putExtra("flag", 1);

        } else if ((view.getTag() + "").compareTo("driverlicence") == 0) {
            intent = new Intent(DocumentSelectActivity.this, OtherActivity.class);
            intent.putExtra("flag", 2);
        } else {
            intent = new Intent(DocumentSelectActivity.this, OtherActivity.class);
            intent.putExtra("flag", 3);
        }
        startActivity(intent);
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
