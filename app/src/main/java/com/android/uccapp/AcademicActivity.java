package com.android.uccapp;

import android.support.v4.app.Fragment;

public class AcademicActivity extends SuperFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AcademicFragment();
    }
}
