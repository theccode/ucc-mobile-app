package com.android.uccapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Course;
import com.android.uccapp.model.Grade;
import com.android.uccapp.model.GradeBook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ResultFragment extends Fragment {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private TableLayout mTableLayout;
    private TableRow mGradeRow;
    private TextView mCourseCodeTextView;
    private TextView mCourseTitleTextView;
    private TextView mCRTextView;
    private TextView mGDTextView;
    private TextView mGPTextView;
    private TextView mAcademicYearTextView;
    private List<String> mCourseNames;
    private List<String> mCreditHours;
    private static final String ARG_REGNO = "com.android.uccapp.regno";
    private String mRegistrationNumber;
    private HashMap<String, String> gradeMap = new HashMap<>();
    private HashMap<String, String> creditWeight = new HashMap<>();


    public static ResultFragment newInstance(String registrationNumber){
        Bundle arg = new Bundle();
        arg.putString(ARG_REGNO, registrationNumber);
        ResultFragment resultFragment = new ResultFragment();
        resultFragment.setArguments(arg);
        return resultFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("studentsGradeBook", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mRegistrationNumber = getArguments().getString(ARG_REGNO);
        creditWeight.put("A", "4.0");
        creditWeight.put("B+", "3.5");
        creditWeight.put("B", "3.0");
        creditWeight.put("C+", "2.5");
        creditWeight.put("C", "2.0");
        creditWeight.put("D+", "1.5");
        creditWeight.put("D", "1.0");
        creditWeight.put("E", "0.00");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        init( view);
        return view;
    }

    private void init(final View view){
        final HashMap<String, String> gradeMap = new HashMap<>();
        mDatabaseReference.child(mRegistrationNumber).child("courseCodesAndGrades").child("0").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot data: dataSnapshot.getChildren()){
                    gradeMap.put(data.getKey(), data.getValue().toString());
                    ConfigUtility.createFirebaseUtil("courses", getActivity());
                    DatabaseReference courseRef = ConfigUtility.mFirebaseReference;

                    courseRef.child(data.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mTableLayout = view.findViewById(R.id.tableLayout1);
                            mGradeRow = new TableRow(getActivity());

                            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                            layoutParams.setMargins(1, 1, 1, 1);
                            final TableRow.LayoutParams tvLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                            tvLayoutParams.setMargins(2, 0, 2, 0);
                            mGradeRow.setLayoutParams(layoutParams);
                            mGradeRow.setBackgroundColor(Color.parseColor("#c8e8ff"));
                            mAcademicYearTextView = new TextView(getContext());
                            mGDTextView = new TextView(getContext());
                            mGDTextView.setTextColor(Color.parseColor("#233F8F"));
                            mGDTextView.setLayoutParams(tvLayoutParams);
                            mCourseCodeTextView = new TextView(getContext());
                            mCourseCodeTextView.setLayoutParams(tvLayoutParams);
                            mCourseCodeTextView.setTextColor(Color.parseColor("#233F8F"));

                            mGDTextView =  new TextView(getContext());
                            mGDTextView.setLayoutParams(tvLayoutParams);
                            mGDTextView.setTextColor(Color.parseColor("#233F8F"));
                            mGPTextView = new TextView(getContext());
                            mGPTextView.setLayoutParams(tvLayoutParams);
                            mGPTextView.setTextColor(Color.parseColor("#233F8F"));
                            mCourseTitleTextView = new TextView(getContext());
                            mCourseTitleTextView.setLayoutParams(tvLayoutParams);
                            mCourseTitleTextView.setTextColor(Color.parseColor("#233F8F"));
                            mCRTextView = new TextView(getContext());
                            mCRTextView.setLayoutParams(tvLayoutParams);
                            mCRTextView.setTextColor(Color.parseColor("#233F8F"));
                            Course course = dataSnapshot.getValue(Course.class);
                            Log.d("NAME", course.getCourseName());
                            Log.d("HOUR", course.getCreditHours());
                            mCourseTitleTextView.setText(course.getCourseName());
                            Iterator it = creditWeight.entrySet().iterator();
                            float gp = 0.0f;
                            while (it.hasNext()){
                                Map.Entry itemPair =  (Map.Entry) it.next();
                                String key = (String) itemPair.getKey();
                                String value = (String) itemPair.getValue();
                                if (data.getValue().equals(key)){
                                    mGPTextView.setText(String.valueOf(Double.parseDouble(value) * Double.parseDouble(course.getCreditHours().split(" ")[0])));
                                }
                            }
                            mCRTextView.setText(course.getCreditHours().split(" ")[0]);
                            mCourseCodeTextView.setText(data.getKey());
                            mGDTextView.setText(data.getValue().toString());
                            mGradeRow.addView(mCourseCodeTextView);
                            mGradeRow.addView(mCourseTitleTextView);
                            mGradeRow.addView(mCRTextView);
                            mGradeRow.addView(mGDTextView);
                            mGradeRow.addView(mGPTextView);
                            mTableLayout.addView(mGradeRow);
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
    private void setAcademicYear(final TableLayout tableLayout,  final TableLayout.LayoutParams tblParams){
        final TableRow tableRow = new TableRow(getContext());
        final TextView tv =  new TextView(getContext());
        ConfigUtility.createFirebaseUtil("courseRegistrationOpening", getActivity());
        DatabaseReference academicYearRef = ConfigUtility.mFirebaseReference;
        academicYearRef.child("academicYear").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv.setText(dataSnapshot.getValue().toString());
                tableRow.setLayoutParams(tblParams);
                tableRow.addView(tv);
                tableLayout.addView(tableRow);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
