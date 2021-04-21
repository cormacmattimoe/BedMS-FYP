package com.example.bedms;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class GetAllBeds {

    public GetAllBeds(){

    }

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
    BedInfo bed = new BedInfo();
    ArrayList<BedInfo> allBedsArray = new ArrayList<>();



    public GetAllBeds(ArrayList<BedInfo> allBedsArray) {
        this.allBedsArray = allBedsArray;
    }



        public void callback(Task<QuerySnapshot> querySnapShot)
        {
            for (QueryDocumentSnapshot bedDetail : querySnapShot.getResult()) {
                bed.setBedId(bedDetail.getId());
                bed.setWard(bedDetail.getString("Ward"));
                int statusCode = convertStatus(bedDetail.getString("Status"));
                bed.setCurrentStatus(statusCode);
                allBedsArray.add(bed);
            }
        };


    //history here...


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
}