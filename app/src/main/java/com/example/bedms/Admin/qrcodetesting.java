package com.example.bedms.Admin;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.R;
import com.example.bedms.qrMainScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class qrcodetesting extends AppCompatActivity {
    public final static int QRCodeWidth = 500;
    public final static int QRCodeHeight = 500;
    private static final String TAG = "qrcoding";

    Bitmap bitmap;
    private EditText bedName;
    private Button downloadBtn;
    private Button generateBtn;
    private ImageView iv;
    String bednameSearch, wardnameSearch;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> bedList =  new ArrayList<>();
    String QR_CODE_IMAGE_PATH = "./" + bednameSearch + ".png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodetesting);
        bedName = findViewById(R.id.text);
        downloadBtn = findViewById(R.id.download);
        downloadBtn.setVisibility(View.INVISIBLE);
        generateBtn = findViewById(R.id.generate);
        iv = findViewById(R.id.image);

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bedName.getText().toString().trim().length() == 0){
                    Toast.makeText(qrcodetesting.this, "Enter Text", Toast.LENGTH_SHORT).show();
                }
                else{
                    try{
                        retrieveBedId();
                        bitmap = textToImageEncode(bednameSearch);
                        iv.setImageBitmap(bitmap);
                        downloadBtn.setVisibility(View.VISIBLE);
                        downloadBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                             //   MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "code_scanner", null);
                                String QR_CODE_IMAGE_PATH =  bednameSearch + ".png";
                                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                                File file = new File(directory, bednameSearch + ".png");
                                if (!file.exists()) {
                                    Log.d("path", file.toString());
                                    FileOutputStream fos = null;
                                    try {
                                        fos = new FileOutputStream(file);
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                        fos.flush();
                                        fos.close();
                                    } catch (java.io.IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Toast.makeText(qrcodetesting.this, "Saved to gallery", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                    }catch (WriterException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void retrieveBedId() {
        bednameSearch = bedName.getText().toString();
        db.collection("bed")
                .whereEqualTo("BedName", bednameSearch)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String bedIdd = document.getId();
                                System.out.println("This is id :" + bedIdd);
                                bedList.add(bedIdd);
                                System.out.println("This is id outside :" + bedIdd);
                            }
                            System.out.println("Bed id" + " : " + bedList);
                            if (bedList == null) {
                                System.out.println("No bed found");
                                Toast.makeText(getApplicationContext(), "No bed has been found please re - enter details", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(qrcodetesting.this, qrMainScreen.class);
                                startActivity(i);
                            }

                            System.out.println("Bed id bedReturned" + bedList);
                            //   assignBedsListToQrCode(bedList);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private Bitmap textToImageEncode(String value) throws WriterException {
        BitMatrix bitMatrix;

        try {
            bitMatrix = new MultiFormatWriter().encode(value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE, QRCodeWidth, QRCodeHeight, null);
        } catch (IllegalArgumentException e) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offSet = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offSet + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}