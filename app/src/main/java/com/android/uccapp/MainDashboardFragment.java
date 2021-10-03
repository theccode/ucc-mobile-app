package com.android.uccapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class MainDashboardFragment extends Fragment {
    private ImageView mAcademicsImageView;
    private ImageView mProfileImageView;
    private ImageView mRegisterImageView;
    private TextView mUserNameTextView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private BottomNavigationView mBottomNavigationView;
    private static final String ARG_USER = "user_Id";
    private User mUser;
    private final String BUNDLE_USER = "com.android.uccapp.userId";

    public static MainDashboardFragment newInstance(User user){
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_USER, (Serializable) user);
        MainDashboardFragment mainDashboardFragment = new MainDashboardFragment();
        mainDashboardFragment.setArguments(arg);
        return mainDashboardFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("student", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mUser = (User) getArguments().getSerializable(ARG_USER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_dashboard, container, false);
        if (savedInstanceState != null){
            mUser = (User) savedInstanceState.getSerializable(BUNDLE_USER);
        }
        mBottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mAcademicsImageView = (ImageView) view.findViewById(R.id.ivAcademics);
        mUserNameTextView = (TextView) view.findViewById(R.id.tvUserName);
        mUserNameTextView.setText(mUser.getFirstName());
        mAcademicsImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), AcademicActivity.class);
                startActivity(intent);
//                getActivity().finish();
            }
        });
        mProfileImageView = (ImageView) view.findViewById(R.id.ivProfile);
        mProfileImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent showStudentsProfile = new Intent(getActivity(), StudentProfileActivity.class);
                startActivity(showStudentsProfile);
//                getActivity().finish();
            }
        });
        mRegisterImageView = (ImageView) view.findViewById(R.id.ivRegister);
        mRegisterImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = RegisterActivity.newIntent(getContext(), mUser);
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
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            return false;
        }
    };
}
