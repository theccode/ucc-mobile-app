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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class FinancierDashboardFragment extends Fragment {
    private LinearLayout mLinearLayout;
    private ImageView mProfileImageView;
    private TextView mFirstNameTextView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Toolbar mToolbar;
    private User mUser;
    private CountDownTimer mCountDownTimer;
    private ProgressDialog mProgressDialog;
    private int i = 0;
    private BottomNavigationView mBottomNavigationView;
    private static final String ARG_USER = "com.android.uccapp.user";

    public static FinancierDashboardFragment newInstance(User user){
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_USER, user);
        FinancierDashboardFragment financierFragment = new FinancierDashboardFragment();
        financierFragment.setArguments(arg);
        return financierFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("cashBook", getActivity());
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
        View view = inflater.inflate(R.layout.fragment_financier_dashboar, container, false);
        mBottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        init(view);
        return view;
    }

    private void init(View view) {
        mLinearLayout = (LinearLayout) view.findViewById(R.id.llFinancialSettings);
        mProfileImageView = (ImageView) view.findViewById(R.id.ivProfileImage);
        mFirstNameTextView = (TextView) view.findViewById(R.id.tvFirstName);
        mFirstNameTextView.setText("Hi " + mUser.getFirstName() + ", Welcome back!");
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeeListActivity.class);
                startActivity(intent);
            }
        });
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
