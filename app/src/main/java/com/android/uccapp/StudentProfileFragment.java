package com.android.uccapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StudentProfileFragment extends Fragment {
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
    private Student mStudent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);
        ConfigUtility.createFirebaseUtil("students", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mStudent = ConfigUtility.mStudent;
        mFullNameEditText = (EditText) view.findViewById(R.id.etFullName);
        mFullNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //TODO
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mStudent.setStudentsName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO
            }
        });
        mProgrammeEditText = (EditText) view.findViewById(R.id.etProgramme);
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
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
//        mDatabaseReference.setValue(mStudent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
//                mFullNameEditText.setText(student.getStudentsName());
                mProgrammeEditText.setText(student.getProgramme());
                mMajorEditText.setText(student.getMajor());
                mLevelEditText.setText(student.getLevel());
                mGenderEditText.setText(student.getGender());
                mDateOfBirthEditText.setText(student.getDateOfBirth().toString());
                mEmailAdressEditText.setText(student.getEmailAddresss());
                mPhoneEditText.setText(student.getPhone());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("Worked", "Working!");
    }
}
