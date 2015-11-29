package identitycrises.com.identitycrisis.UI;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import identitycrises.com.identitycrisis.R;
import identitycrises.com.identitycrisis.imagehandler.ImageManager;
import identitycrises.com.identitycrisis.imagehandler.InvalidPathException;

@TargetApi(16)
public class OtherActivity extends AppCompatActivity {
    private TextView dataText;
    private ImageView frontImg, backImg, selfie;
    private ImageManager imgManager;
    private Button btnDone;

    private Uri frontUri, backUri,selfieUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        imgManager = new ImageManager(this);
        btnDone = (Button) findViewById(R.id.btnDone);
        dataText = (TextView) findViewById(R.id.txt_header);
        frontImg = (ImageView) findViewById(R.id.img_front);
        backImg = (ImageView) findViewById(R.id.img_back);
        selfie = (ImageView) findViewById(R.id.selfie_img);
        int choice = getIntent().getIntExtra("flag", 0);
        switch (choice) {
            case 1:
                dataText.setText("Upload passport images");
                break;
            case 2:
                dataText.setText("Upload driver's license images");
                break;
            case 3:
                dataText.setText("Upload voter ID images");
                break;
        }

        frontImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imgManager.captureImage(1);
                } catch (InvalidPathException e) {
                    e.printStackTrace();
                }
            }
        });
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imgManager.captureImage(2);
                } catch (InvalidPathException e) {
                    e.printStackTrace();
                }
            }
        });
        selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imgManager.captureSelfieImage(3);
                } catch (InvalidPathException e) {
                    e.printStackTrace();
                }
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtherActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Toast.makeText(OtherActivity.this, "Your documents will be validated shortly", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

    }

    void onRegister(View view) {
        Intent intent = new Intent(OtherActivity.this, DocumentSelectActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            Uri uri = imgManager.onActivityResult(requestCode,resultCode,data);

            if(requestCode == 1){
                frontImg.setImageURI(uri);
            }
            else if (requestCode == 2){
                backImg.setImageURI(uri);
            }
            else {
                selfie.setImageURI(uri);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
