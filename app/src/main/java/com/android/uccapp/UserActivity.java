package com.android.uccapp;

import android.support.v4.app.Fragment;

public class UserActivity extends SuperFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new UserFragment();
    }
}