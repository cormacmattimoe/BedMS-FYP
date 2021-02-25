package com.example.bedms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bedms.Admin.qrcodetesting;

public class qrMainScreen extends AppCompatActivity {
    //vars
    public static final int CAMERA_PERMISSION_CODE = 100;

    //widgets
    private Button camera, generate, scan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_main_screen);
        camera = findViewById(R.id.camera);
        generate = findViewById(R.id.generate);
        scan = findViewById(R.id.scanButton);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            }
        });
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qrMainScreen.this, qrcodetesting.class);
                startActivity(intent);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qrMainScreen.this, readQrCode.class);
                startActivity(intent);
            }
        });
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
}
