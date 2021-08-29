package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

public class DepartmentActivity extends SuperFragmentActivity {
    private static final String EXTRA_DEPARTMENT_CODE = "com.android.uccapp.departmentId";

    public static Intent newIntent(Context packageContext, String departmentCode){
        Intent intent = new Intent(packageContext, DepartmentActivity.class);
        intent.putExtra(EXTRA_DEPARTMENT_CODE, departmentCode);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        String department_code = getIntent().getStringExtra(EXTRA_DEPARTMENT_CODE);
        return DepartmentFragment.newInstance(department_code);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}