package com.android.uccapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Course;
import com.android.uccapp.model.CourseForRegistration;
import com.android.uccapp.model.Department;
import com.android.uccapp.model.TimeTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CreateTimeTableFragment extends Fragment {
    private Spinner mDepartmentSpinner;
    private Toolbar mToolbar;
    private Spinner mLevelSpinner;
    private Spinner mCourseCodeSpinner;
    private EditText mCourseNameEditText;
    private Button mDateButton;
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private EditText mVenueEditText;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private TimeTable mTimeTable;
    private TextView mLevelTextView;
    private String[] mlevels = new String[]{"100", "200", "300", "400", "500", "600", "700", "800"};
    private ArrayAdapter<String> mLevelsAdapter;
    private static final String DIALOGUE_DATE = "dialogueDate";
    private static final String DIALOGUE_TIME = "dialogueTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_START_TIME = 1;
    private static final int REQUEST_FINISH_TIME = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("timetable", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mTimeTable = new TimeTable();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.fragment_create_time_table, container, false);
        mToolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDepartmentSpinner = (Spinner) view.findViewById(R.id.spDepartmentName);
        mLevelTextView = (TextView) view.findViewById(R.id.tvLevel);
        mCourseNameEditText = (EditText) view.findViewById(R.id.etCourseName);
        ConfigUtility.createFirebaseUtil("departments", getActivity());
        DatabaseReference depRef = ConfigUtility.mFirebaseReference;
        depRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> depNames = new ArrayList<>();
                    ArrayAdapter<String> mDepartmentAdapter;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Department department = data.getValue(Department.class);
                    depNames.add(department.getDepartmentName());
                }
                mDepartmentAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, depNames);
                mDepartmentSpinner.setAdapter(mDepartmentAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mLevelsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mlevels);
//        mLevelSpinner.setAdapter(mLevelsAdapter);
        mCourseCodeSpinner = (Spinner) view.findViewById(R.id.spCourseCode);
        ConfigUtility.createFirebaseUtil("RegistrableCourses", getActivity());
        final DatabaseReference courseRef = ConfigUtility.mFirebaseReference;
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> codes = new ArrayList<>();
                for (DataSnapshot datae:dataSnapshot.getChildren()){
                    for (final DataSnapshot data:datae.getChildren()){
                        final CourseForRegistration registrableCourse = data.getValue(CourseForRegistration.class);
                        codes.add(registrableCourse.getCourseCode());
                        mDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {

                                ConfigUtility.createFirebaseUtil("RegistrableCourses", getActivity());
                                DatabaseReference depRef = ConfigUtility.mFirebaseReference;
                                depRef.child(adapterView.getItemAtPosition(i).toString().replaceAll("\\s+", "")).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                            List<String> selectedCodes = new ArrayList<>();
                                            ArrayAdapter<String>  courseAdapter;
                                        for (DataSnapshot data:dataSnapshot.getChildren()){
                                            CourseForRegistration registrableCourse = data.getValue(CourseForRegistration.class);
                                            for (int i = 0; i < codes.size(); i++){
                                                if (registrableCourse.getCourseCode().equals(codes.get(i))){
                                                    selectedCodes.add(codes.get(i));
                                                }
                                            }
                                        }
                                        if(selectedCodes.isEmpty()) mLevelTextView.setText(null);
                                        courseAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, selectedCodes);
                                        mCourseCodeSpinner.setAdapter(courseAdapter);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        mCourseCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(final AdapterView<?> adapterView, View view, final int i, long l) {
                                ConfigUtility.createFirebaseUtil("courses", getActivity());
                                DatabaseReference courseReference = ConfigUtility.mFirebaseReference;
                                courseReference.child(adapterView.getItemAtPosition(i).toString()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Course course = dataSnapshot.getValue(Course.class);

                                        if (course != null){
                                            mCourseNameEditText.setText(course.getCourseName());
                                            mCourseNameEditText.setKeyListener(null);
                                            mLevelTextView.setText(course.getLevel());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDateButton = (Button) view.findViewById(R.id.btnExamDate);
        mDateButton.setText(mTimeTable.getExamDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dialogue = DatePickerFragment.newInstance(mTimeTable.getExamDate());
                FragmentManager fragmentManager = getFragmentManager();

                dialogue.setTargetFragment(CreateTimeTableFragment.this, REQUEST_DATE);
                dialogue.show(fragmentManager, DIALOGUE_DATE);
            }
        });
        mStartTimeButton = (Button) view.findViewById(R.id.btnStartTime);
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDispatcher(REQUEST_START_TIME);
            }
        });
        mEndTimeButton = (Button) view.findViewById(R.id.btnEndTime);
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDispatcher(REQUEST_FINISH_TIME);
            }
        });
        mVenueEditText = (EditText) view.findViewById(R.id.etExamVenue);
        mVenueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTimeTable.setVenue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            mTimeTable.setExamDate(date);
            mDateButton.setText(mTimeTable.getExamDate().toString());
        } else if (requestCode == REQUEST_START_TIME){
            String time = (String) data.getStringExtra(TimePickerFragment.EXTRA_TIME);
            mTimeTable.setStartTime(time);
            mStartTimeButton.setText(mTimeTable.getStartTime());
        } else if (requestCode == REQUEST_FINISH_TIME){
            String time = (String) data.getStringExtra(TimePickerFragment.EXTRA_TIME);
            mTimeTable.setEndTime(time);
            mEndTimeButton.setText(mTimeTable.getEndTime());
        }
    }
    private void timeDispatcher(final int timeConstraint){
        FragmentManager manager = getFragmentManager();
        TimePickerFragment timeDialogue = new TimePickerFragment();
        timeDialogue.setTargetFragment(CreateTimeTableFragment.this, timeConstraint);
        timeDialogue.show(manager, DIALOGUE_TIME);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimeTable != null){
            mTimeTable.setDepartmentName(mDepartmentSpinner.getSelectedItem().toString());
            mTimeTable.setCourseCode(mCourseCodeSpinner.getSelectedItem().toString());
            mTimeTable.setCourseName(mCourseNameEditText.getText().toString());
            mTimeTable.setLevel(mLevelTextView.getText().toString());
            ConfigUtility.createFirebaseUtil("examTimeTable", getActivity());
            DatabaseReference timeTableRef = ConfigUtility.mFirebaseReference;
            timeTableRef.child(mTimeTable.getDepartmentName().replaceAll("\\s+", "")).child(mTimeTable.getLevel()).child(mTimeTable.getCourseCode()).setValue(mTimeTable);
        }
    }
}
