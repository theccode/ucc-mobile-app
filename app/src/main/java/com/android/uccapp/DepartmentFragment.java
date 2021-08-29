package com.android.uccapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DepartmentFragment extends Fragment {
    private EditText mDepartmentCodeEditText;
    private EditText mDepartmentNameEditText;
    private EditText mSchoolOrCollegeEditText;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Department mDepartment;
    private String mDepartmentCode;
    private Toolbar mToolbar;

    private static final String ARG_DEPARTMENT_CODE = "com.android.uccapp.department_id";

    public static DepartmentFragment newInstance(String department_code){
        Bundle args = new Bundle();
        args.putString(ARG_DEPARTMENT_CODE, department_code);
        DepartmentFragment departmentFragment = new DepartmentFragment();
        departmentFragment.setArguments(args);
        return departmentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("departments", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department, container, false);
        mDepartment = new Department();
        mToolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDepartmentCode = (String) getArguments().getSerializable(ARG_DEPARTMENT_CODE);
        setHasOptionsMenu(true);
        mDepartmentCodeEditText = view.findViewById(R.id.etDepartmentCode);
        mDepartmentCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mDepartment.setDepartmentCode(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mDepartmentNameEditText = view.findViewById(R.id.etDepartmentName);
        mDepartmentNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mDepartment.setDepartmentName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSchoolOrCollegeEditText = view.findViewById(R.id.etCollegeOrSchool);
        mSchoolOrCollegeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mDepartment.setCollegeOrSchool(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (mDepartmentCode != null){
            mDatabaseReference.child(mDepartmentCode).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        mDepartment = dataSnapshot.getValue(Department.class);
                        if (mDepartment != null){
                            mDepartmentCodeEditText.setText(mDepartment.getDepartmentCode());
                            mDepartmentCodeEditText.setKeyListener(null);
                            mDepartmentNameEditText.setText(mDepartment.getDepartmentName());
                            mSchoolOrCollegeEditText.setText(mDepartment.getCollegeOrSchool());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            setHasOptionsMenu(false);
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDepartment != null){
            if (mDepartment.getDepartmentCode() != null){
                mDatabaseReference.child(mDepartment.getDepartmentCode()).setValue(mDepartment);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.remove_department_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.remove_department:
                mDatabaseReference.child(mDepartmentCode).removeValue();
                mDepartment = null;
                startActivity(new Intent(getActivity(), DepartmentListActivity.class));
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }
}
