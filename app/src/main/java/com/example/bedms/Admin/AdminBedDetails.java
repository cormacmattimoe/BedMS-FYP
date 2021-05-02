package com.example.bedms.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Model.Bed;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static com.example.bedms.Admin.qrcodetesting.QRCodeHeight;
import static com.example.bedms.Admin.qrcodetesting.QRCodeWidth;

public class AdminBedDetails extends AppCompatActivity {
    private static final String TAG = "updateobs";
    TextView tvType, tvPatientId, tvStatus, tvWard;
    ImageView iv;
    BottomNavigationView bottomnav;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String bedId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_details);
        setTitle("Bed Details");
        tvType = findViewById(R.id.tvName);
        tvPatientId = findViewById(R.id.tvPatientId);
        tvStatus = findViewById(R.id.tvStatus);
        tvWard = findViewById(R.id.tvWard);
        iv = findViewById(R.id.image);

        Intent intent = getIntent();
        String bedName;
        bedName = intent.getStringExtra("Name");
        tvType.setText(bedName);
        // showObstructionDetails();
        Intent i = getIntent();
        bedName = i.getStringExtra("BedName");
        tvType.setText(bedName);


        db.collection("bed")
                .whereEqualTo("BedName", bedName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Bed tempBed = null;
                            int counter = 0;
                            task.getResult();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                                bedId = document.getId();
                                counter = counter + 1;
                                tvStatus.setText(document.getString("Status"));
                                tvWard.setText(document.getString("Ward"));
                                tvPatientId.setText(document.getString("PatientID"));

                            Bitmap bitmap = null;
                            try {
                                bitmap = textToImageEncode(document.getString("BedName"));
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                            iv.setImageBitmap(bitmap);
                            }
                        }
                    }
                });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adminhospital_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AdminBedDetails.this, AdminHub.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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