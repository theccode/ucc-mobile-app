package com.android.uccapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Department;
import com.android.uccapp.model.GradeBook;
import com.android.uccapp.model.RegisteredStudents;
import com.android.uccapp.model.Staff;
import com.android.uccapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisteredStudentsListFragment extends Fragment {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private StudentsGradeAdapter mStudentsGradeAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final String ARG_USER = "com.android.uccapp.user";
    private User mUser;
    public static RegisteredStudentsListFragment newInstance(User user){
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        RegisteredStudentsListFragment registeredStudentsListFragment = new RegisteredStudentsListFragment();
        registeredStudentsListFragment.setArguments(args);
        return registeredStudentsListFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("registeredCourses", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mUser = (User) getArguments().getSerializable(ARG_USER);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses_result_list, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.courses_result_list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private class StudentsGradeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RegisteredStudents mRegisteredStudents;
        private GradeBook mGradeBook;
        private String mCourseCode;
        private TextView mCourseTitleTextView;
        private TextView mCourseCodeTextView;
        private TextView mCoursesCreditHours;

        public StudentsGradeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_courses_result_list_item, parent, false));
            mCourseTitleTextView = (TextView) itemView.findViewById(R.id.tvProgram);
            mCourseCodeTextView = (TextView) itemView.findViewById(R.id.tvFeeAmount);
            mCoursesCreditHours = (TextView) itemView.findViewById(R.id.tvLevel);
            itemView.setOnClickListener(this);
        }

        public  void bind(final GradeBook gradeBook){
            String gradeKey = null;
            HashMap<String, String> tmpGrades;
            final List<String> courseCodeList = new ArrayList<>();
            for (int i = 0; i < gradeBook.getCourseCodesAndGrades().size(); i++){
                tmpGrades = gradeBook.getCourseCodesAndGrades().get(i);
                courseCodeList.addAll(tmpGrades.keySet());
            }
            mCoursesCreditHours.setText(courseCodeList.toString().split(",")[0]);

            ConfigUtility.createFirebaseUtil("staff", getActivity());
            DatabaseReference staffRef = ConfigUtility.mFirebaseReference;
            final String finalCourseCode = gradeKey;
            staffRef.child(mUser.getRegistrationNumber().replace("/", "_")).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("STAFFS", dataSnapshot.getValue().toString());
                    Staff staff = (Staff) dataSnapshot.getValue(Staff.class);
                   for(int i = 0; i < courseCodeList.size(); i++){
                       if (courseCodeList.contains(staff.getCourse())){
                           mCourseCode = staff.getCourse();
                           mGradeBook = gradeBook;
                           mCoursesCreditHours.setText(staff.getCourse());
                           mCourseTitleTextView.setText(gradeBook.getRegistrationNumber().replace("_", "/"));
                       }
                   }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        @Override
        public void onClick(View view){
            Intent intent = GradeBookActivity.newIntent(getContext(),mGradeBook.getRegistrationNumber(), mCourseCode );
            startActivity(intent);
        }
    }

    private class StudentsGradeAdapter extends RecyclerView.Adapter<StudentsGradeHolder>{
        List<GradeBook> mGradeBooks;
        public StudentsGradeAdapter(List<GradeBook> gradeBooks){
            mGradeBooks = gradeBooks;
        }
        @NonNull
        @Override
        public StudentsGradeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new StudentsGradeHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull StudentsGradeHolder studentsGradeHolder, int i) {
            studentsGradeHolder.bind(mGradeBooks.get(i));
        }

        @Override
        public int getItemCount() {
            return mGradeBooks.size();
        }
    }

    private void updateUI(){
        ConfigUtility.createFirebaseUtil("departments", getActivity());
        DatabaseReference depRef = ConfigUtility.mFirebaseReference;
        Log.d("DEP", mUser.getRegistrationNumber().split("/")[1]);
        depRef.child(mUser.getRegistrationNumber().split("/")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Department department = dataSnapshot.getValue(Department.class);
                ConfigUtility.createFirebaseUtil("studentsGradeBook", getActivity());
                final DatabaseReference studentsGradeBookRef = ConfigUtility.mFirebaseReference;
                final String departmentName = department.getDepartmentName().replaceAll("\\s+", "");
                mDatabaseReference.child(departmentName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("DATASNAP", dataSnapshot.toString());
                        studentsGradeBookRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                GradeBook gradeBook;
                                List<GradeBook> gradeBooks = new ArrayList<>();
                                for (DataSnapshot data : dataSnapshot.getChildren()){
                                    gradeBook = data.getValue(GradeBook.class);
                                    gradeBooks.add(gradeBook);
                                }
                                if (mStudentsGradeAdapter == null){
                                    mStudentsGradeAdapter = new StudentsGradeAdapter(gradeBooks);
                                    mRecyclerView.setAdapter(mStudentsGradeAdapter);
                                } else {
                                    mStudentsGradeAdapter.notifyDataSetChanged();
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
