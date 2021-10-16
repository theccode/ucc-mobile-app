package com.android.uccapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Grade;
import com.android.uccapp.model.GradeBook;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GradeBookFragment extends Fragment {
    private final static String ARG_GRADE = "com.android.uccapp.grade";
    private final static String ARG_REGNUMBER = "com.android.uccapp.regNum";
    private Toolbar mToolbar;
    private String mCourseCode;
    private String mRegistrationNumber;
    private EditText mGradeEditText;
    private TextView mRegistrationNumberTextView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private GradeBook mGradeBook;
    private Grade mGrade;
    public static GradeBookFragment newInstance(String coursCode, String registrationNumber){
        Bundle args = new Bundle();
        args.putString(ARG_GRADE, coursCode);
        args.putString(ARG_REGNUMBER, registrationNumber);
        GradeBookFragment gradeBookFragment = new GradeBookFragment();
        gradeBookFragment.setArguments(args);
        return gradeBookFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCourseCode = getArguments().getString(ARG_GRADE);
        mRegistrationNumber = getArguments().getString(ARG_REGNUMBER);
        ConfigUtility.createFirebaseUtil("studentsGradeBook", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mGradeBook = new GradeBook();
        mGrade = new Grade();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade_book, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(mCourseCode);

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mGradeEditText = (EditText) view.findViewById(R.id.etGrade);
        mRegistrationNumberTextView = (TextView) view.findViewById(R.id.tvStudentId);
        mRegistrationNumberTextView.setText(mRegistrationNumber.replace("_", "/"));
        init(mGradeEditText);

        return view;
    }
    private void init(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mGrade.setGrade(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGradeBook != null){
            mDatabaseReference.child(mRegistrationNumber.replace("/", "_")).child("courseCodesAndGrades").child("0").child(mCourseCode).setValue(mGrade.getGrade());
        }
    }
}
