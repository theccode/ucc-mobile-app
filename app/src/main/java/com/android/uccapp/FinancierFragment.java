package com.android.uccapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Department;
import com.android.uccapp.model.Student;
import com.android.uccapp.model.StudentsFee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinancierFragment extends Fragment {
    private Spinner mDepartmentSpinner;
    private EditText mProgramEditText;
    private Spinner mLevelSpinner;
    private EditText mFeeAmountEditText;
    private Button mDateButton;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private StudentsFee mStudentsFee;
    private Toolbar mToolbar;
    private ArrayAdapter<String> mDepartmentArrayAdapter;
    private ArrayAdapter<String> mLevelsArrayAdapter;
    private String[] mLevelsList = new String[]{"100", "200", "300", "400", "500", "600", "700", "800"};

    private static final String ARG_FEE = "com.android.uccapp.fee";

    public static FinancierFragment newInstance(StudentsFee fee){
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_FEE, fee);
        FinancierFragment financierFragment = new FinancierFragment();
        financierFragment.setArguments(arg);
        return financierFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("studentsFee", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mStudentsFee = new StudentsFee();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_financier, container, false);
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setTitle("Finance");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init(view);
        return view;
    }

    private void init(View view) {
        mProgramEditText = view.findViewById(R.id.etProgram);
        mProgramEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudentsFee.setProgram(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mFeeAmountEditText = view.findViewById(R.id.etFeeAmount);
        mFeeAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudentsFee.setFeeAmount(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mDepartmentSpinner = (Spinner) view.findViewById(R.id.spDepartmentName);
        mLevelSpinner = (Spinner) view.findViewById(R.id.spLevel);
        mLevelsArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mLevelsList);
        mLevelSpinner.setAdapter(mLevelsArrayAdapter);
        ConfigUtility.createFirebaseUtil("departments", getActivity());
        DatabaseReference depRef = ConfigUtility.mFirebaseReference;
        depRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> departmentNameList = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        Department department = data.getValue(Department.class);
                        departmentNameList.add(department.getDepartmentName());
                    }
                }
                mDepartmentArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, departmentNameList);
                mDepartmentSpinner.setAdapter(mDepartmentArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        StudentsFee studentsFee = (StudentsFee) getArguments().getSerializable(ARG_FEE);
        if (studentsFee != null){
            mDepartmentSpinner.setSelection(getIndex(mDepartmentSpinner, studentsFee.getDepartment()));
            mLevelSpinner.setSelection(getIndex(mLevelSpinner, studentsFee.getLevel()));
            mProgramEditText.setText(studentsFee.getProgram());
            mFeeAmountEditText.setText(studentsFee.getFeeAmount());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mStudentsFee != null){
            mStudentsFee.setDepartment(mDepartmentSpinner.getSelectedItem().toString());
            mStudentsFee.setLevel(mLevelSpinner.getSelectedItem().toString());
            mDatabaseReference.child(mStudentsFee.getDepartment().replaceAll("\\s+","")).child(mStudentsFee.getLevel()).setValue(mStudentsFee);
        }
    }
    private int getIndex(Spinner spinner, String item){
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equals(item)){
                return i;
            }
        }
        return 0;
    }
}
