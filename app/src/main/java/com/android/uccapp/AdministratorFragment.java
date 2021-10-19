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
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdministratorFragment extends Fragment {
    private ImageView mStudentImageView;
    private ImageView mCoursesImageView;
    private ImageView mDepartmentImageView;
    private ImageView mRegistrationImageView;
    private ImageView mResultsImageView;
    private ImageView mTimetableImageView;
    private ImageView mUsersImageView;
    private LinearLayout mStaffLinearLayout;
    private Toolbar mToolbar;
    private TextView mFirstNameTextView;
    private BottomNavigationView mBottomNavigationView;
    private static final String ARG_USER = "com.android.uccapp.user";
    private CircleImageView mProfileCircleImageView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private User mUser;
    private CountDownTimer mCountDownTimer;
    private ProgressDialog mProgressDialog;
    private int i = 0;

    public static AdministratorFragment newInstance(User user){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_USER, user);
        AdministratorFragment administratorFragment = new AdministratorFragment();
        administratorFragment.setArguments(bundle);
        return administratorFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User) getArguments().getSerializable(ARG_USER);
        ConfigUtility.createFirebaseUtil("student", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
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
        View view = inflater.inflate(R.layout.fragment_administrator, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mBottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mStudentImageView = view.findViewById(R.id.ivStudentsButton);
        mStudentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StudentsListActivity.class);
                startActivity(intent);
            }
        });
        mCoursesImageView = (ImageView) view.findViewById(R.id.ivCourses);
        mCoursesImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CourseListActivity.newIntent(getContext(), mUser.getRegistrationNumber());
                startActivity(intent);
            }
        });
        mDepartmentImageView = (ImageView) view.findViewById(R.id.ivDepartment);
        mDepartmentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DepartmentListActivity.class);
                startActivity(intent);
            }
        });
        mRegistrationImageView = (ImageView) view.findViewById(R.id.ivRegistration);
        mRegistrationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegistrationLinkActivity.class));
            }
        });
        mResultsImageView = (ImageView) view.findViewById(R.id.ivResult);
        mResultsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), ResultsActivity));
                Toast.makeText(getActivity(), "Results! //TODO", Toast.LENGTH_LONG).show();
            }
        });
        mTimetableImageView = (ImageView) view.findViewById(R.id.ivTimetable);
        mTimetableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateTimetableActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "TimeTable //TODO", Toast.LENGTH_LONG).show();
            }
        });
        mUsersImageView = (ImageView) view.findViewById(R.id.ivUsers);
        mUsersImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserListActivity.class));
            }
        });
        mFirstNameTextView = (TextView) view.findViewById(R.id.etFirstName);
        mFirstNameTextView.setText("Hi " +mUser.getFirstName()+ ", Welcome back!");
        mProfileCircleImageView = view.findViewById(R.id.tvProfileImage);

        mDatabaseReference.child(mUser.getRegistrationNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                Log.d("STUDENT", student.getPhotoUrl());
                Picasso.with(getContext()).load(student.getPhotoUrl()).resize(60,60).centerCrop().into(mProfileCircleImageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mStaffLinearLayout = (LinearLayout) view.findViewById(R.id.staff);
        mStaffLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StaffListActivity.class);
                startActivity(intent);
            }
        });
        return view;
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
