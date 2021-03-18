package com.example.bedms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bedms.Admin.qrcodetesting;
import com.example.bedms.Auth.login;
import com.example.bedms.CleaningStaff.bedDetailsCleaner;
import com.example.bedms.CleaningStaff.cleaningstaffhub;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

public class qrMainScreen extends AppCompatActivity implements View.OnClickListener {
    //vars
    public static final int CAMERA_PERMISSION_CODE = 100;

    //widgets
    private Button camera, generate, scan;
    ImageView scanBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_main_screen);
        generate = findViewById(R.id.generate);
        scanBtn = findViewById(R.id.imageBtn);
        scanBtn.setOnClickListener(this);

    }

    public void checkPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(qrMainScreen.this, permission)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(qrMainScreen.this, new String[] {permission},
                    requestCode);
        }
        else{
            Toast.makeText(this, "Permission Already Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                // messageText.setText(intentResult.getContents());
                String bedName = intentResult.getContents();

                // Intent intent = getIntent();
                //  String str;
                // str = intent.getStringExtra(bedName);
                //  messageText.setText(str);
                //   messageFormat.setText(intentResult.getFormatName());
                Intent i = new Intent(qrMainScreen.this, bedDetailsCleaner.class);
                i.putExtra("BedName", bedName);
                startActivity(i);
                finish();
            }
        } else {
            //  super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a QR Code");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        intentIntegrator.initiateScan();
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospital_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Intent i = new Intent(qrMainScreen.this, cleaningstaffhub.class);
                startActivity(i);
                return true;

            case R.id.item2:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(qrMainScreen.this, login.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
