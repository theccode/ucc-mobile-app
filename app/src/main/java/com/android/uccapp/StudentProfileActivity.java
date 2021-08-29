package com.android.uccapp;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StudentProfileActivity extends SuperFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new StudentProfileFragment();
    }
}