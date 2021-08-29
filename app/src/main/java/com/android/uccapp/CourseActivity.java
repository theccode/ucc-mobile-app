package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

public class CourseActivity extends SuperFragmentActivity  {
    private static final String EXTRA_COURSE_CODE = "com.android.uccapp.course_code";
    public static Intent newIntent(Context packageContext, String courseCode){
        Intent intent = new Intent(packageContext, CourseActivity.class);
        intent.putExtra(EXTRA_COURSE_CODE, courseCode);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        String courseCode = getIntent().getStringExtra(EXTRA_COURSE_CODE);
        return  CourseFragment.newInstance(courseCode);
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