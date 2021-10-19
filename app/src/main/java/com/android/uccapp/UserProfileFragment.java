package com.android.uccapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Student;
import com.android.uccapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class UserProfileFragment extends Fragment {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ConfigUtility ref;
    private EditText mFullNameEditText;
    private EditText mProgrammeEditText;
    private EditText mMajorEditText;
    private EditText mGenderEditText;
    private EditText mDateOfBirthEditText;
    private EditText mLevelEditText;
    private EditText mEmailAdressEditText;
    private EditText mPhoneEditText;
    private EditText mHallOfResidenceEditText;
    private EditText mRoomNumberEditText;
    private EditText mCurrentAddressEditText;
    private Student mStudent;
    private User mUser;
    private Toolbar mToolbar;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final String ARG_USER = "com.android.uccapp.user";

    public static UserProfileFragment newInstance(User user){
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_USER, user);
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        userProfileFragment.setArguments(arg);
        return userProfileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User) getArguments().getSerializable(ARG_USER);
        mStudent = new Student();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (mUser != null){
            mToolbar.setTitle(mUser.getRegistrationNumber().replace("_", "/"));
        }
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ConfigUtility.createFirebaseUtil("student", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mFullNameEditText = (EditText) view.findViewById(R.id.etFullName);
        mStudent.setFirstName(mUser.getFirstName());
        mStudent.setLastName(mUser.getLastName());
        mProgrammeEditText = (EditText) view.findViewById(R.id.etCourse);
        mProgrammeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setProgramme(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO
            }
        });
        mMajorEditText = (EditText) view.findViewById(R.id.etMajor);
        mMajorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setMajor(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mGenderEditText = (EditText) view.findViewById(R.id.etGender);
        mGenderEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setGender(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO
            }
        });
        mDateOfBirthEditText = (EditText) view.findViewById(R.id.etDateOfBirth);

        mLevelEditText = (EditText) view.findViewById(R.id.etLevel);
        mLevelEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setLevel(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO
            }
        });
        mEmailAdressEditText = (EditText) view.findViewById(R.id.etEmailAddress);
        mEmailAdressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setEmailAddresss(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPhoneEditText = (EditText) view.findViewById(R.id.etPhone);
        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setPhone(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mHallOfResidenceEditText = (EditText) view.findViewById(R.id.etHallOfResidence);
        mHallOfResidenceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setHallOfResidence(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mRoomNumberEditText = (EditText) view.findViewById(R.id.etRoomNumber);
        mRoomNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setRoomNumber(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mCurrentAddressEditText = (EditText) view.findViewById(R.id.etCurrentResidenceAddress);
        mCurrentAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setCurrentResidenceAddress(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
            mStudent.setStudentsId(mUser.getRegistrationNumber());
        try {
            mStudent.setDateOfBirth(mSimpleDateFormat.parse(mDateOfBirthEditText.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDatabaseReference.child(mUser.getRegistrationNumber()).setValue(mStudent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUser != null){
            mDatabaseReference.child(mUser.getRegistrationNumber()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()){
                       Student student = dataSnapshot.getValue(Student.class);
                       if (student != null){
                           mFullNameEditText.setText(student.getFirstName() + " " +student.getLastName());
                           mFullNameEditText.setKeyListener(null);
                           mProgrammeEditText.setText(student.getProgramme());
                           mProgrammeEditText.setKeyListener(null);
                           mMajorEditText.setText(student.getMajor());
                           mMajorEditText.setKeyListener(null);
                           mLevelEditText.setText(student.getLevel());
                           mLevelEditText.setKeyListener(null);
                           mGenderEditText.setText(student.getGender());
                           mGenderEditText.setKeyListener(null);
                           mDateOfBirthEditText.setText(student.getDateOfBirth().toString());
                           mDateOfBirthEditText.setKeyListener(null);
                           mEmailAdressEditText.setText(student.getEmailAddresss());
                           mPhoneEditText.setText(student.getPhone());
                           mHallOfResidenceEditText.setText(student.getHallOfResidence());
                           mHallOfResidenceEditText.setKeyListener(null);
                           mRoomNumberEditText.setText(student.getRoomNumber());
                           mRoomNumberEditText.setKeyListener(null);
                           mCurrentAddressEditText.setText(student.getCurrentResidenceAddress());
                       }
                   }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("STATEUSER", mUser);
    }
}
