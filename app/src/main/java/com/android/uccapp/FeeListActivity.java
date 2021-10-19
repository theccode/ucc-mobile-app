package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.uccapp.model.StudentsFee;

public class FeeListActivity extends SuperFragmentActivity {
    private static final String EXTRA_FEE = "com.android.fee";

    public static Intent newIntent(Context packageContext, StudentsFee fee){
        Intent intent = new Intent(packageContext, FeeListActivity.class);
        intent.putExtra(EXTRA_FEE, fee);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        StudentsFee fee = (StudentsFee) getIntent().getSerializableExtra(EXTRA_FEE);
        return FeeListFragment.newInstance(fee);
    }
}