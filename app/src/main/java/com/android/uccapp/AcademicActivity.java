package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class AcademicActivity extends SuperFragmentActivity {
    private static final String EXTRA_REGNO = "com.android.uccapp.regno";

    public static Intent newIntent(Context packageContext, String registrationNumber){
        Intent intent = new Intent(packageContext, AcademicActivity.class);
        intent.putExtra(EXTRA_REGNO, registrationNumber);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        String registrationNumber = getIntent().getStringExtra(EXTRA_REGNO);
        return  AcademicFragment.newInstance(registrationNumber);
    }
}
