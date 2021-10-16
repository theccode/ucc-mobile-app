package com.android.uccapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Department;
import com.android.uccapp.model.Student;
import com.android.uccapp.model.TimeTable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class TimeTableFragment extends Fragment {
    private TableLayout mTableLayout;
    private TableRow mTableRow;
    private TextView mCourseCodeTextView;
    private TextView mCourseTitleTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;
    private TextView mVenueTextView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private TimeTable mTimeTable;
    private String mRegistrationNumber;
    private static final String ARG_REGNO = "com.android.uccapp.regno";
    private static final String ARG_TOOLBAR = "com.android.uccapp.toolbar";
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static TimeTableFragment newInstance(String registrationNumber){
        Bundle arg = new Bundle();
        arg.putString(ARG_REGNO, registrationNumber);
        TimeTableFragment timeTableFragment = new TimeTableFragment();
        timeTableFragment.setArguments(arg);
        return timeTableFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("examTimeTable", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mTimeTable = new TimeTable();
        mRegistrationNumber = getArguments().getString(ARG_REGNO);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);
        init(view);
        return view;
    }

    private void init(final View view) {
        Log.d("REG", mRegistrationNumber.split("_")[1]);
        ConfigUtility.createFirebaseUtil("departments", getActivity());
        DatabaseReference depRef = ConfigUtility.mFirebaseReference;
        depRef.child(mRegistrationNumber.split("_")[1]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Department department = dataSnapshot.getValue(Department.class);
                ConfigUtility.createFirebaseUtil("student", getActivity());
                DatabaseReference studRef = ConfigUtility.mFirebaseReference;
                studRef.child(mRegistrationNumber).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            Student student = dataSnapshot.getValue(Student.class);
                            mDatabaseReference.child(department.getDepartmentName().replaceAll("\\s+", "")).child(student.getLevel()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot data:dataSnapshot.getChildren()){
                                        mTableLayout = view.findViewById(R.id.tableLayout);
                                        mTableRow = new TableRow(getActivity());

                                        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                                        layoutParams.setMargins(1, 1, 1, 1);
                                        TableRow.LayoutParams tvLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                                        tvLayoutParams.setMargins(2, 0, 2, 0);
                                        String tvColor = "#233F8F";
                                        mTableRow.setLayoutParams(layoutParams);
                                        mTableRow.setBackgroundColor(Color.parseColor("#c8e8ff"));
                                        mCourseCodeTextView = new TextView(getActivity());
                                        mCourseCodeTextView.setLayoutParams(tvLayoutParams);
                                        mCourseCodeTextView.setTextColor(Color.parseColor(tvColor));
                                        mCourseTitleTextView = new TextView(getActivity());
                                        mCourseTitleTextView.setLayoutParams(tvLayoutParams);
                                        mCourseTitleTextView.setTextColor(Color.parseColor(tvColor));
                                        mDateTextView = new TextView(getActivity());
                                        mDateTextView.setLayoutParams(tvLayoutParams);
                                        mDateTextView.setTextColor(Color.parseColor(tvColor));
                                        mTimeTextView = new TextView(getActivity());
                                        mTimeTextView.setLayoutParams(tvLayoutParams);
                                        mTimeTextView.setTextColor(Color.parseColor(tvColor));
                                        mVenueTextView = new TextView(getActivity());
                                        mVenueTextView.setLayoutParams(tvLayoutParams);
                                        mVenueTextView.setTextColor(Color.parseColor(tvColor));
                                        TimeTable timeTable = data.getValue(TimeTable.class);
                                        mCourseCodeTextView.setText(timeTable.getCourseCode());
                                        mCourseTitleTextView.setText(timeTable.getCourseName());
                                        mDateTextView.setText(mSimpleDateFormat.format(timeTable.getExamDate()));
                                        mTimeTextView.setText(timeTable.getStartTime() + "-" + timeTable.getEndTime());
                                        mVenueTextView.setText(timeTable.getVenue());
                                        mTableRow.addView(mCourseCodeTextView);
                                        mTableRow.addView(mCourseTitleTextView);
                                        mTableRow.addView(mDateTextView);
                                        mTableRow.addView(mTimeTextView);
                                        mTableRow.addView(mVenueTextView);
                                        mTableLayout.addView(mTableRow);
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
