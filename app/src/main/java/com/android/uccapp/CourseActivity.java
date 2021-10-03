package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

public class CourseActivity extends SuperFragmentActivity  {
    private static final String EXTRA_COURSE_CODE = "com.android.uccapp.courseCode";
    private static final String EXTRA_USERID = "com.android.uccapp.userId";
    public static Intent newIntent(Context packageContext, String courseCode, String userId){
        Intent intent = new Intent(packageContext, CourseActivity.class);
        intent.putExtra(EXTRA_COURSE_CODE, courseCode);
        intent.putExtra(EXTRA_USERID, userId);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        String courseCode = getIntent().getStringExtra(EXTRA_COURSE_CODE);
        String userId = getIntent().getStringExtra(EXTRA_USERID);
        return  CourseFragment.newInstance(courseCode, userId);
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