package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.uccapp.model.User;

public class AdministratorActivity extends SuperFragmentActivity{
    private static final String EXTRA_USER = "com.android.uccapp.user_id";

    public static Intent newIntent(Context packageContext, User user){
        Intent intent  = new Intent(packageContext, AdministratorActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }
    @Override
    protected Fragment createFragment(){
        User user = (User) getIntent().getSerializableExtra(EXTRA_USER);
        return AdministratorFragment.newInstance(user);
    }
}