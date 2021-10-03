package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class CourseListActivity extends SuperFragmentActivity {
    private static final String EXTRA_USERID = "com.android.uccapp.userId";

    public  static Intent newIntent(Context packageContext, String userId){
        Intent intent = new Intent(packageContext, CourseListActivity.class);
        intent.putExtra(EXTRA_USERID, userId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String userId = getIntent().getStringExtra(EXTRA_USERID);
        return CoursesListFragment.newInstance(userId);
    }
}
