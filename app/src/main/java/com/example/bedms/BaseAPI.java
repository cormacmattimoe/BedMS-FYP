package com.example.bedms;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.Executor;

public class BaseAPI {


    FirebaseFirestore db;

    // A new instance of this callback must be passed to the get method by the caller
    public interface GetApiCallback {
        void onResponse(boolean success, Task<QuerySnapshot> data);
    }

    // get some resource from the Firestore
    public void get(String collection, GetApiCallback callback) {

        db = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> data = new Task<QuerySnapshot>() {
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

        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> listReturned) {
                        // inside this callback is where you could make subsequent asynchronous api calls
                        if (listReturned.isSuccessful()) {
                            // success so return to consumer
                            callback.onResponse(true, listReturned);
                        }
                        else {
                            // api call failed so return an error
                            callback.onResponse(false, null);
                        }
                    }
                });
    }

    // could define other methods here for updating, creating, deleting, etc.

}
