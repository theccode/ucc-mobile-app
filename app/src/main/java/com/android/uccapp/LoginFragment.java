package com.android.uccapp;

import android.annotation.SuppressLint;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private EditText mIndexNumberEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private User mUser;
    private CountDownTimer mCountDownTimer;
    private ProgressDialog mProgressDialog;
    private int i = 0;
    private Animation mScaledDownAnim, mScaledUpAnim;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("users", getActivity());
        mUser = ConfigUtility.mUser;
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mFirebaseAuth = ConfigUtility.mFirebaseAuth;
        mAuthStateListener = ConfigUtility.mFirebaseAuthStateListener;
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("...fetching dashboard...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(i);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        mScaledDownAnim = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
        mScaledUpAnim = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mIndexNumberEditText = view.findViewById(R.id.etIndex);
        mPasswordEditText = view.findViewById(R.id.etPassword);
        mLoginButton = view.findViewById(R.id.btnLogin);
//        mIndexNumberEditText.setText("PS/CSC/17/0060"); mPasswordEditText.setText("8cff7c3f-d22"); admin
//        mIndexNumberEditText.setText("PS/CSC/STAFF/0013"); mPasswordEditText.setText("0ce2e482-0eb"); staff
//        mIndexNumberEditText.setText("PS/CSC/STAFF/0014"); mPasswordEditText.setText("69aaa62c-ade"); staff
//        mIndexNumberEditText.setText("UCC/STAFF/FIN/0001"); mPasswordEditText.setText("9c3c0f84-d51"); finance officer
//        mIndexNumberEditText.setText("PS/CSC/17/0006"); mPasswordEditText.setText("80ec76f4-9a6"); //student
        mIndexNumberEditText.setText("PS/CSC/17/0004"); mPasswordEditText.setText("2d833105-c87"); //student
        mLoginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    mLoginButton.setAnimation(mScaledUpAnim);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    mLoginButton.setAnimation(mScaledDownAnim);
                }
                final String index = mIndexNumberEditText.getText().toString().replace("/", "_").toUpperCase();
                final String password = mPasswordEditText.getText().toString();

                Log.d("INDEX", index);
                if (!index.isEmpty() && !password.isEmpty()){
//                    User user = UserFactory.get(getContext()).getUser(index);
                    ConfigUtility.createFirebaseUtil("users", getActivity());
                    DatabaseReference databaseReference = ConfigUtility.mFirebaseReference;
                    databaseReference.child(index).addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            mUser = user;
                            if (null != mUser){
                                if (mUser.getPassword().equals(password)){
                                    if (mUser.isAdmin()) {
                                        Intent intent = AdministratorActivity.newIntent(getContext(), mUser);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        Objects.requireNonNull(getActivity()).finish();
                                    } else if (mUser.isLecturer()){
                                        Intent  intent =  LecturerDashboardActivity.newIntent(getActivity(), mUser);
                                        startActivity(intent);
                                    } else if (mUser.isFinancier()) {
                                        Intent intent = FinancierDashboardActivity.newIntent(getContext(), mUser);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = MainDashboardActivity.newIntent(getContext(), mUser);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        Objects.requireNonNull(getActivity()).finish();
                                    }

                                    mProgressDialog.show();
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
                                } else {
                                    Toast.makeText(getContext(), "Incorrect Index or Password", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "User doesn't exist!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    if (index.isEmpty()){
                        Toast.makeText(getContext(), "Registration Number field cannot be empty!", Toast.LENGTH_LONG).show();
                    }
                    else if (password.isEmpty()){
                        Toast.makeText(getContext(), "Password field cannot be empty!", Toast.LENGTH_LONG).show();
                    }
                }

                return true;
            }
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
