package com.example.bedms.HospitalManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bedms.Admin.AdminHub;
import com.example.bedms.Model.Bed;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static com.example.bedms.Admin.qrcodetesting.QRCodeHeight;
import static com.example.bedms.Admin.qrcodetesting.QRCodeWidth;

public class CalculateScreenBedDetails extends AppCompatActivity {
    private static final String TAG = "updateobs";
    TextView tvBedId, tvBedName, tvPatientId, tvStatus, tvWard;
    ImageView iv;
    BottomNavigationView bottomnav;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String bedId;
    String bedIdTrans;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentSnapshot document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_detailscal);
        setTitle("Bed Details");
        tvBedName = findViewById(R.id.tvBedNamecal);
        tvBedId = findViewById(R.id.tvBedIdCal);
        tvPatientId = findViewById(R.id.tvPatientId);
        tvStatus = findViewById(R.id.tvStatus);
        tvWard = findViewById(R.id.tvWard);
        iv = findViewById(R.id.image);

        Intent intent = getIntent();

        bedIdTrans = intent.getStringExtra("BedId");
        tvBedId.setText(bedIdTrans);


        db.collection("bed")
                .document(bedIdTrans)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        Bed tempBed = null;
                        int counter = 0;
                        counter = counter + 1;
                        tvBedName.setText(documentSnapshot.getString("BedName"));
                        tvStatus.setText(documentSnapshot.getString("Status"));
                        tvWard.setText(documentSnapshot.getString("Ward"));
                        tvPatientId.setText(documentSnapshot.getString("PatientID"));

                        Bitmap bitmap = null;
                        try {
                            bitmap = textToImageEncode(documentSnapshot.getId());
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                        iv.setImageBitmap(bitmap);
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
                Intent i = new Intent(CalculateScreenBedDetails.this, AdminHub.class);
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