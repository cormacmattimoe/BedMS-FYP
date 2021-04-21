/*

package com.example.bedms;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class GetAllBedsFromDb {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GetAllBeds gab = new GetAllBeds();

// callback interface

    interface GetAllBedsfromDBCallback {
        void callback(Task<QuerySnapshot> listReturned);
    }


    public void getListofBeds(GetAllBedsfromDBCallback getAllBedsfromDBCallback) {
        db.collection("bed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> listReturned) {
                        if (listReturned.isSuccessful()) {
                            getAllBedsfromDBCallback.callback(listReturned);
                        }
                        gab.callback(listReturned);
                    }
                });
    }

}

 */

