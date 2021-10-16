package com.android.uccapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.uccapp.model.User;

public class LecturerDashboardFragment extends Fragment {
    private LinearLayout mLecturersCoursesLinearLayout;
    private TextView mFirstNameTextView;
    private static final String ARG_USER = "com.android.uccapp.user";
    private static final String BUNDLE_USER = "com.android.uccapp.user";
    private User mUser;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecturer_dashboard, container, false);
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
}
