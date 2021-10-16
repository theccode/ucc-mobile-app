package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.android.uccapp.model.Staff;
import com.android.uccapp.model.User;

public class LecturerDashboardActivity extends SuperFragmentActivity {
    private static final String EXTRA_STAFF = "com.andrroid.uccapp.staff";

    public static Intent  newIntent(Context packageContext, User user){
        Intent intent = new Intent(packageContext, LecturerDashboardActivity.class);
        intent.putExtra(EXTRA_STAFF, user);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        User user = (User) getIntent().getSerializableExtra(EXTRA_STAFF);
        return LecturerDashboardFragment.newInstance(user);
    }
}