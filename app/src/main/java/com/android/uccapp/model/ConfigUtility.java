package com.android.uccapp.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigUtility {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mFirebaseReference;
    public static FirebaseStorage mFirebaseStorage;
    public static StorageReference mFirebaseStorageReference;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mFirebaseAuthStateListener;
    public static ConfigUtility mConfigUtility;
    private static final int LOGIN_REQUEST_CODE = 0;
    private static Activity caller;
    public static Student mStudent;
    public static User mUser;
    public static Course mCourse;

    private ConfigUtility(){}

    public static void createFirebaseUtil(String ref, Activity lCaller){
        caller = lCaller;
        if (mConfigUtility == null){
            mConfigUtility = new ConfigUtility();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase.setPersistenceEnabled(true);
        }
        mFirebaseReference = mFirebaseDatabase.getReference().child(ref);
        connectStorage();
        mUser = new User();
    }

    private static void connectStorage(){
        mFirebaseStorage = FirebaseStorage.getInstance();
        mFirebaseStorageReference = mFirebaseStorage.getReference("students_photos");
    }
    private void signIn(){
        mFirebaseAuth.signInWithCustomToken("mycustomtoke").addOnCompleteListener(caller, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("TAG", "signInWithToken: Success");
                }
            }
        });
    }
}
