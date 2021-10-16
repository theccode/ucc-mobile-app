package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;

public class GradeBookActivity extends SuperFragmentActivity {
    private final static String EXTRA_REGISTRATION = "com.android.uccapp.grade";
    private final static String EXTRA_CODE = "com.android.uccapp.courseCode";

    public static Intent newIntent(Context packageContext, String courseCode, String registrationNumber){
        Intent intent = new Intent(packageContext, GradeBookActivity.class);
        intent.putExtra(EXTRA_REGISTRATION, registrationNumber);
        intent.putExtra(EXTRA_CODE, courseCode);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        String registrationNumber = getIntent().getStringExtra(EXTRA_REGISTRATION);
        String courseCode = getIntent().getStringExtra(EXTRA_CODE);
        return GradeBookFragment.newInstance(registrationNumber, courseCode);
    }
}