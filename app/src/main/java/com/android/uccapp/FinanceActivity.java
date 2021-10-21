package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.uccapp.model.User;

public class FinanceActivity extends SuperFragmentActivity {

    private final static String EXTRA_USER = "com.android.uccapp";

    public static Intent newIntent(Context packageContext, User user){
        Intent intent = new Intent(packageContext, FinanceActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        User user = (User) getIntent().getSerializableExtra(EXTRA_USER);
        return FinanceFragment.newInstance(user);
    }
}