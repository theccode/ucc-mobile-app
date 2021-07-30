package com.android.uccapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Arrays;
import java.util.List;

public class ConfigUtility {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mFirebaseReference;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mFirebaseAuthStateListener;
    public static ConfigUtility mConfigUtility;
    private static final int LOGIN_REQUEST_CODE = 0;
    private static Activity caller;

    private ConfigUtility(){}

    public static void createFirebaseUtil(String ref, Activity lCaller){
        caller = lCaller;
        if (mConfigUtility == null){
            mConfigUtility = new ConfigUtility();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (mFirebaseAuth.getCurrentUser() == null){
                        logIn();
                    }
                }
            };
        }
        mFirebaseReference = mFirebaseDatabase.getReference().child(ref);
    }
    private static void logIn(){

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
        );
        caller.startActivityForResult(
                AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers).build(),
                LOGIN_REQUEST_CODE
        );
    }
    public static void attachAuthStateListener(){
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthStateListener);
    }
    public static void detachAuthStateListener(){
        mFirebaseAuth.removeAuthStateListener(mFirebaseAuthStateListener);
    }
}
