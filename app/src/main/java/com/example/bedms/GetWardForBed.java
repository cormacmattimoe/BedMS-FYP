
package com.example.bedms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetWardForBed {
    String ward;
    FirebaseFirestore db =  FirebaseFirestore.getInstance();
    String bedId;
    public GetWardForBed(){


    }

    public String GetWard(String bedId) {
        System.out.println(" bebinning of GetWard " + bedId);

        db.collection("bed")
                .document(bedId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      //  System.out.println( " after db call" + bedDetails);
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            ward = document.getString("Ward");
                        }
                    }
                });
        System.out.println("this is ward returned = " + ward);
        return ward;
    }
}