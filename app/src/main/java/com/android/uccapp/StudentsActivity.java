package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

public class StudentsActivity extends SuperFragmentActivity {
    private static final String EXTRA_STUDENTS_ID = "com.android.uccapp.index";


    public static Intent newIntent(Context packageContext, String index){
        Intent intent = new Intent(packageContext, StudentsActivity.class);
        intent.putExtra(EXTRA_STUDENTS_ID, index);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        String studentsId = getIntent().getStringExtra(EXTRA_STUDENTS_ID);
        return  StudentsFragment.newInstance(studentsId);
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
