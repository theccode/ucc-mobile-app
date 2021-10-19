package com.android.uccapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.User;

import java.util.Objects;

public class LecturerDashboardFragment extends Fragment {
    private LinearLayout mLecturersCoursesLinearLayout;
    private TextView mFirstNameTextView;
    private static final String ARG_USER = "com.android.uccapp.user";
    private static final String BUNDLE_USER = "com.android.uccapp.user";
    private User mUser;
    private BottomNavigationView mBottomNavigationView;
    private CountDownTimer mCountDownTimer;
    private ProgressDialog mProgressDialog;
    private int i = 0;
    public static LecturerDashboardFragment newInstance(User user){
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        LecturerDashboardFragment lecturerDashboardFragment = new LecturerDashboardFragment();
        lecturerDashboardFragment.setArguments(args);
        return lecturerDashboardFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            mUser = (User) savedInstanceState.getSerializable(BUNDLE_USER);
        }
        mUser = (User) getArguments().getSerializable(ARG_USER);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Thanks for your patronage, Bye!...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(i);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecturer_dashboard, container, false);
        mBottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mFirstNameTextView  = (TextView) view.findViewById(R.id.etFirstName);
        mFirstNameTextView.setText("Hi " + mUser.getFirstName() + ", Welcome back!");
        mLecturersCoursesLinearLayout = (LinearLayout) view.findViewById(R.id.llCourses);
        mLecturersCoursesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = RegisteredStudentsListActivity.newIntent(getContext(), mUser);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_USER, mUser);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.signout:
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                    if (mProgressDialog != null){
                        mProgressDialog.show();
                    }
                    mCountDownTimer = new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            if (mProgressDialog != null){
                                mProgressDialog.dismiss();
                                mProgressDialog = null;
                            }
                        }
                    }.start();
            }
            return false;
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
