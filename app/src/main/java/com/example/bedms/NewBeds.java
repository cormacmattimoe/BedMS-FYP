package com.example.bedms;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bedms.Model.Bed;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class NewBeds {

    public BaseAPI baseApi;
    BedInfo bed = new BedInfo();
    ArrayList<BedInfo> allBedsArray = new ArrayList<>();
    Task<QuerySnapshot> querySnapShot = new Task<QuerySnapshot>() {
        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public boolean isCanceled() {
            return false;
        }

        @Nullable
        @Override
        public QuerySnapshot getResult() {
            return null;
        }

        @Nullable
        @Override
        public <X extends Throwable> QuerySnapshot getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnSuccessListener(@NonNull OnSuccessListener<? super QuerySnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super QuerySnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super QuerySnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };



    // create new instance of BaseApi when initialising the class
    NewBeds() {
        baseApi = new BaseAPI();
    }
    // handle a click on a button
    public void testing() {
        baseApi.get("bed", new BaseAPI.GetApiCallback() {
            @Override
            public void onResponse(boolean success, Task<QuerySnapshot> data) {
                if (success) {
                    // now we have access to the `data` where we can populate the UI
                    for (QueryDocumentSnapshot bedDetail : querySnapShot.getResult()) {
                        bed.setBedId(bedDetail.getId());
                        bed.setWard(bedDetail.getString("Ward"));
                        int statusCode = convertStatus(bedDetail.getString("Status"));
                        bed.setCurrentStatus(statusCode);
                        allBedsArray.add(bed);
                    }
                    // next to add history or
                   for(BedInfo b : allBedsArray){
                        System.out.println("This is bed number " + b.getBedId());
                    }
                } else {
                    // show the user an error on the UI
                }
            }
        });
    }
    public int convertStatus(String bedStatus){
        //Get in event-type and returns an integer for status
        int statusCode;
        switch (bedStatus) {
            case "Added bed":
            case "Bed allocated to ward":
            case "Bed is now open":
            case "Bed is cleaned – ready for next patient":
                statusCode = 0;
                break;
            case "Bed allocated - patient on way":
                statusCode = 1;
                break;
            case "Patient in bed in ward":
                statusCode = 2;
                break;
            case "Bed ready for cleaning":
                statusCode = 3;
                break;
            default:
                statusCode = 4;
                break;
        }
        return statusCode;
    }

    public static void main(String[] args){
        NewBeds nd = new NewBeds();
        nd.testing();
    }
}
