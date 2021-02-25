/*
package com.example.bedms.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.example.bedms.Auth.welcome;
import com.example.bedms.Bed.managebeds;
import com.example.bedms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class printbedlabels extends AppCompatActivity {


    String bednameSearch, wardnameSearch;
    private static final String TAG = "labels";
    EditText bedName, wardName;
    Button labelBtn;
    BinaryBitmap qrCode;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List <String> bedList =  new ArrayList<>();
    String QR_CODE_IMAGE_PATH = "./" + bednameSearch + ".png";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printbedlabels);
        setTitle("Print Bed labels");

        bedName = findViewById(R.id.bedNameEd);
        wardName = findViewById(R.id.wardNameEd);
        labelBtn = findViewById(R.id.printBtn);



        labelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveBedId();


                //Access member variable
            //    System.out.println("These are" + bedList.get(0));





                //Call api with bedId and it will bring back the qr code iamge to print
               // printlabels("ICU2", "St Joes", "0s9DBoYTnK3rd1jvojEl", "qrCode");





            }
        });


    }


    public void retrieveBedId() {
        bednameSearch = bedName.getText().toString();
        wardnameSearch = wardName.getText().toString();
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
                                Intent i = new Intent(printbedlabels.this, printbedlabels.class);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void assignBedsListToQrCode(List <String> bedList){
        String QR_CODE_IMAGE_PATH = "./" + bednameSearch + ".png";
        String bedId = bedList.get(0);
        int width = 350;
        int height = 350;

        try {
           // generateQrCode(bedId, width, height);
            createQR(bedId, QR_CODE_IMAGE_PATH,
                    wardnameSearch,bednameSearch, height, width));
            System.out.println("Code generated ");
          //  printlabels(bednameSearch,wardnameSearch,bedId,generateQrCode(bedId, width, height));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //  System.out.println("Bed qr " + bedNametoQrCode.get(i));
    }
    // Function to read the QR file
    public static String readQR(String path, String charset,
                                Map hashMap)
            throws FileNotFoundException, IOException,
            NotFoundException
    {
        BinaryBitmap binaryBitmap
                = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(
                                new FileInputStream(path)))));

        Result result
                = new MultiFormatReader().decode(binaryBitmap);

        return result.getText();
    }

    // Driver code
    public static void main(String[] args)
            throws WriterException, IOException,
            NotFoundException
    {

        // Path where the QR code is saved


        // Encoding charset
        String charset = "UTF-8";

        Map<EncodeHintType, ErrorCorrectionLevel> hashMap
                = new HashMap<EncodeHintType,
                ErrorCorrectionLevel>();

        hintMap.put(EncodeHintType.ERROR_CORRECTION,
                ErrorCorrectionLevel.L);

        System.out.println(
                "QRCode output: "
                        + readQRCode(filePath, charset, hintMap));
    }


    // Decode Qr code here
    private String decodeQRCode(BinaryBitmap qrCode) throws IOException {

        try {
            Result result = new MultiFormatReader().decode(qrCode);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }



    //Generate Qr Codes here
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        System.out.println("Bed id = " + text + "Bit matrix" + bitMatrix);


        private Bitmap generateQrCode(String text, int width, int height) throws WriterException, NullPointerException {
            BitMatrix bitMatrix;
            try {
                bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.DATA_MATRIX.QR_CODE,
                        width, height, null);
            } catch (IllegalArgumentException Illegalargumentexception) {
                return null;
            }



            int bitMatrixWidth = bitMatrix.getWidth();
            int bitMatrixHeight = bitMatrix.getHeight();
            int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

            int colorWhite = 0xFFFFFFFF;
            int colorBlack = 0xFF000000;

            for (int y = 0; y < bitMatrixHeight; y++) {
                int offset = y * bitMatrixWidth;
                for (int x = 0; x < bitMatrixWidth; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

            bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
            return bitmap;
        }

    private class generateQrcode extends AsyncTask<String, Void, Bitmap> {
        public final static int WIDTH = 400;
        ImageView bmImage;

        public generateQrcode(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String Value = urls[0];
            com.google.zxing.Writer writer = new QRCodeWriter();
            Bitmap bitmap = null;
            BitMatrix bitMatrix = null;
            try {
                bitMatrix = writer.encode(Value, com.google.zxing.BarcodeFormat.QR_CODE, WIDTH, WIDTH,
                        ImmutableMap.of(EncodeHintType.MARGIN, 1));
                bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < 400; i++) {
                    for (int j = 0; j < 400; j++) {
                        bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK
                                : Color.WHITE);
                    }
                }
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    //Print labels here
    public void printlabels(String bed, String ward, String IdBed, Bitmap qrcodereturn) {


        try {
            String decodedText = decodeQRCode(qrcodereturn);
            if (decodedText == null) {
                System.out.println("No QR Code found in the image");

            } else {
                System.out.println("Decoded text = " + decodedText);
            }
        } catch (IOException e) {
            System.out.println("Could not decode QR Code");
        }
    }



        public static void createQR(String data, String path,
                String charset, Map hashMap,
        int height, int width)
        throws WriterException, IOException
        {

            BitMatrix matrix = new MultiFormatWriter().encode(
                    new String(data.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, width, height);

            MatrixToImageWriter.writeToFile(
                    matrix,
                    path.substring(path.lastIndexOf('.') + 1),
                    new File(path));
        }

        // Driver code
        public static void main(String[] args)
        throws WriterException, IOException,
                NotFoundException
        {

            // The data that the QR code will contain
            String data = "www.geeksforgeeks.org";

            // The path where the image will get saved
            String path = "demo.png";

            // Encoding charset
            String charset = "UTF-8";

            Map<EncodeHintType, ErrorCorrectionLevel> hashMap
                    = new HashMap<EncodeHintType,
                                        ErrorCorrectionLevel>();

            hashMap.put(EncodeHintType.ERROR_CORRECTION,
                    ErrorCorrectionLevel.L);

            // Create the QR code and save
            // in the specified folder
            // as a jpg file
            createQR(data, path, charset, hashMap, 200, 200);
            System.out.println("QR Code Generated!!! ");
        }
    }






    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hospital_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "Home Selected", Toast.LENGTH_LONG).show();
                Intent i = new Intent(printbedlabels.this, managebeds.class);
                startActivity(i);
                return true;

            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent r = new Intent(printbedlabels.this, welcome.class);
                startActivity(r);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
*/