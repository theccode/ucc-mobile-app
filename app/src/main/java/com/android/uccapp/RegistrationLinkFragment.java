package com.android.uccapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Course;
import com.android.uccapp.model.CourseRegisterationOpening;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RegistrationLinkFragment extends Fragment {
    private Button mDeadlineButton;
    private Button mOpenRegistrationButton;
    private EditText mAcademicYearEditText;
    private Toolbar mToolbar;
    private final String DIALOGUE_DATE = "DialogDate";
    private  Spinner mSemesterSpinner;
    private String[] mSemesterList;
    private ArrayAdapter<String> mSemesterArrayAdapter;
    private static final int REQUEST_DATE = 0;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private List<Course> mCourses;
    private Course mCourse;
    private CourseRegisterationOpening mCourseRegisterationOpening;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("courses", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mCourseRegisterationOpening = new CourseRegisterationOpening();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_link, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("Registration");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCourses = new ArrayList<>();
        mCourse = new Course();
        mDeadlineButton = (Button) view.findViewById(R.id.deadlineButton);
        mCourseRegisterationOpening.setDeadline(mDeadlineButton.getText().toString());
        mSemesterSpinner = (Spinner) view.findViewById(R.id.semesterSpinner);
        mAcademicYearEditText = (EditText) view.findViewById(R.id.etStaffId);
        mAcademicYearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCourseRegisterationOpening.setAcademicYear(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mOpenRegistrationButton = (Button) view.findViewById(R.id.btnOpenRegistration);
        mOpenRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mCourse = (Course) dataSnapshot.getValue(Course.class);
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            Course course = (Course) data.getValue(Course.class);
                            course.setIsEnabledForRegistration(true);
                            mCourseRegisterationOpening.setAcademicYear(mCourseRegisterationOpening.getAcademicYear());
                            mDatabaseReference.child(course.getCourseCode()).child("isEnabledForRegistration").setValue(course.getIsEnabledForRegistration());
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("courseRegistrationOpening");
                            dbRef.setValue(mCourseRegisterationOpening);
                            if (course.getIsEnabledForRegistration()) mOpenRegistrationButton.setText("CLOSE REGISTRATION");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Toast.makeText(getActivity(), "Register", Toast.LENGTH_LONG).show();
            }
        });
        mSemesterList = new String[]{"1", "2"};
        mSemesterArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mSemesterList);
        mSemesterSpinner.setAdapter(mSemesterArrayAdapter);
        mCourseRegisterationOpening.setSemester(mSemesterSpinner.getSelectedItem().toString());
        mDeadlineButton.setText(new GregorianCalendar(2021, Calendar.SEPTEMBER, 01).getTime().toString());
        mDeadlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new Date());
                datePickerFragment.setTargetFragment(RegistrationLinkFragment.this, REQUEST_DATE);
                datePickerFragment.show(fm, DIALOGUE_DATE);
            }
        });
        return view;
    }
}
