package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.android.uccapp.model.User;

public class MainDashboardActivity extends SuperFragmentActivity {
    private static String EXTRA_USER = "com.android.uccapp.user";
    public static Intent newIntent(Context packageContext, User user){
        Intent intent = new Intent(packageContext, MainDashboardActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        User user = (User) getIntent().getSerializableExtra(EXTRA_USER);
        return MainDashboardFragment.newInstance(user);
    }
}