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
        mUser = new User();
    }
}
