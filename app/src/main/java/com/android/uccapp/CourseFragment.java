package com.android.uccapp;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Course;
import com.android.uccapp.model.CourseForRegistration;
import com.android.uccapp.model.Department;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CourseFragment extends Fragment {
    private EditText mCourseCodeEditText;
    private EditText mCourseTitleEditText;
    private EditText mCreditHoursEditText;
    private Spinner mDepartmentSpinner;
    private CourseForRegistration mCourseForRegistration;
    private Spinner mLevelSpinner;
    private Spinner mSemesterSpinner;
    private Toolbar mToolbar;
    private Button mAddCourseButton;
    private final String[] mLevels = new String[]{"100", "200", "300", "400", "500", "600", "700", "800"};
    private final String[] mSemesters = new String[]{"1", "2"};
    private ArrayAdapter<String> mLevelArrayAdapter;
    private ArrayAdapter<String> mSemesterArrayAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Course mCourse;
    private String mCourseCode;
    private String mUserId;
    private static final String ARG_COURSE_CODE = "com.android.uccapp.courseCode";
    private static final String ARG_USERID = "com.android.uccapp.userId";
    private static final String BUNDLE_CODE = "com.android.uccapp.bundleCode";

    public static CourseFragment newInstance(String courseCode, String userId){
        Bundle bundle = new Bundle();
        bundle.putString(ARG_COURSE_CODE,courseCode);
        bundle.putString(ARG_USERID, userId);
        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setArguments(bundle);
        return courseFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("courses", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCourseCode = (String) getArguments().getSerializable(ARG_COURSE_CODE);
        if (savedInstanceState != null){
            mUserId = savedInstanceState.getString(BUNDLE_CODE);
        }
        mUserId = (String) getArguments().getString(ARG_USERID);
        mCourse = new Course();
        mCourseForRegistration = new CourseForRegistration();
        setHasOptionsMenu(true);
        mCourseCodeEditText = (EditText) view.findViewById(R.id.etCourseCode);
        mCourseCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCourse.setCourseCode(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mCourseTitleEditText = (EditText) view.findViewById(R.id.etCourseTitle);
        mCourseTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCourse.setCourseName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mCreditHoursEditText = (EditText) view.findViewById(R.id.etCreditHours);
        mCreditHoursEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCourse.setCreditHours(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mLevelSpinner = view.findViewById(R.id.levelSpinner);
        mSemesterSpinner = view.findViewById(R.id.semesterSpinner);
        mLevelArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mLevels);
        mLevelSpinner.setAdapter(mLevelArrayAdapter);
        mSemesterArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mSemesters);
        mSemesterSpinner.setAdapter(mSemesterArrayAdapter);
        mAddCourseButton = (Button) view.findViewById(R.id.btnAddCourse);
        mAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] depCode = mUserId.split("_");
                Log.d("CODE", depCode[1]);
                DatabaseReference depRef = FirebaseDatabase.getInstance().getReference("departments");
                depRef.child(depCode[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DatabaseReference regReference = FirebaseDatabase.getInstance().getReference("RegistrableCourses");
                       mCourseForRegistration.setCourseCode(mCourse.getCourseCode());
                       mCourseForRegistration.setCourseTitle(mCourse.getCourseName());
                       mCourseForRegistration.setNumOfCreditHours(mCourse.getCreditHours());
                        Department department = dataSnapshot.getValue(Department.class);
                        String depName = department.getDepartmentName().replaceAll("\\s+", "");
                       regReference.child(depName).child(mCourse.getCourseCode()).setValue(mCourseForRegistration);
//                regReference.setValue()
                        Log.d("DEP", dataSnapshot.getValue().toString());
                        Log.d("NAME", depName);
//                        Toast.makeText(getActivity(), "" + dataSnapshot.toString(), Toast.LENGTH_LONG ).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        if (mCourseCode != null){
            mDatabaseReference.child(mCourseCode).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mCourse = dataSnapshot.getValue(Course.class);
                    if (mCourse != null){
                        mCourseCodeEditText.setText(mCourse.getCourseCode());
                        mCourseCodeEditText.setKeyListener(null);
                        mCourseTitleEditText.setText(mCourse.getCourseName());
                        mCreditHoursEditText.setText(mCourse.getCreditHours());
                        mLevelArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mLevels);
                        mLevelSpinner.setSelection(getIndex(mLevelSpinner, mCourse.getLevel()));
                        mSemesterSpinner.setSelection(getIndex(mSemesterSpinner, mCourse.getSemester()));
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
        if (mCourse != null){
            mCourse.setLevel(mLevelSpinner.getSelectedItem().toString());
            mCourse.setSemester(mSemesterSpinner.getSelectedItem().toString());
            if (mCourse.getCourseCode() != null){
                mDatabaseReference.child(mCourse.getCourseCode()).setValue(mCourse);
            }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.remove_course,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.remove_course:
                mDatabaseReference.child(mCourseCode).removeValue();
                mCourse = null;
                Intent intent = new Intent(getActivity(), CourseListActivity.class);
                startActivity(intent);
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_CODE, mUserId);
    }
}
