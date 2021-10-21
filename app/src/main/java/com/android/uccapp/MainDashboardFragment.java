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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Student;
import com.android.uccapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Objects;

public class MainDashboardFragment extends Fragment {
    private ImageView mAcademicsImageView;
    private ImageView mProfileImageView;
    private ImageView mRegisterImageView;
    private TextView mUserNameTextView;
    private LinearLayout mFinanceLinearLayout;
    private ImageView mProfileImage;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private BottomNavigationView mBottomNavigationView;
    private static final String ARG_USER = "user_Id";
    private User mUser;
    private final String BUNDLE_USER = "com.android.uccapp.userId";
    private CountDownTimer mCountDownTimer;
    private ProgressDialog mProgressDialog;
    private int i = 0;

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
        View view = inflater.inflate(R.layout.fragment_main_dashboard, container, false);
        if (savedInstanceState != null){
            mUser = (User) savedInstanceState.getSerializable(BUNDLE_USER);
        }
        mBottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mAcademicsImageView = (ImageView) view.findViewById(R.id.ivAcademics);
        mUserNameTextView = (TextView) view.findViewById(R.id.tvFirstName);
        mUserNameTextView.setText("Hi " + mUser.getFirstName() + ", how are you today?");
        mAcademicsImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = AcademicActivity.newIntent(getActivity(), mUser.getRegistrationNumber());
                startActivity(intent);
            }
        });
        mProfileImageView = (ImageView) view.findViewById(R.id.ivProfile);
        mProfileImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent showStudentsProfile = UserProfileActivity.newIntent(getContext(), mUser);
                startActivity(showStudentsProfile);
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
        mProfileImage = (ImageView) view.findViewById(R.id.tvProfileImage);
        mDatabaseReference.child(mUser.getRegistrationNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                Picasso.with(getContext()).load(student.getPhotoUrl()).resize(60,60).centerCrop().into(mProfileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mFinanceLinearLayout = (LinearLayout) view.findViewById(R.id.llFinance);
        mFinanceLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = FinanceActivity.newIntent(getContext(), mUser);
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
