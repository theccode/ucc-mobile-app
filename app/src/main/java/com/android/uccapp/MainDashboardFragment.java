package com.android.uccapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainDashboardFragment extends Fragment {
    private ImageView mAcademicsImageView;
    private ImageView mProfileImageView;
    private ImageView mRegisterImageView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private BottomNavigationView mBottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("student", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_dashboard_fragment, container, false);
        mBottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mAcademicsImageView = (ImageView) view.findViewById(R.id.ivAcademics);
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
                getActivity().finish();
            }
        });
        mRegisterImageView = (ImageView) view.findViewById(R.id.ivRegister);
        mRegisterImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), RegisterStudent.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            return false;
        }
    };
}
