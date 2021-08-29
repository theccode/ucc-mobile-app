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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentsListFragment extends Fragment {
    private RecyclerView mSudentsRecyclerView;
    private StudentsAdapter mStudentsAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("student", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_recycler_view, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSudentsRecyclerView = (RecyclerView) view.findViewById(R.id.student_recycler);
        mSudentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(true);
        init();
        return view;
    }

    private  class StudentsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Student mStudent;
        private TextView mFullNameTextView;
        private TextView mIndexTextView;
        private TextView mLevelTextView;
        public StudentsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.student_list_person, parent, false));

            itemView.setOnClickListener(this);
            mFullNameTextView = (TextView) itemView.findViewById(R.id.tvDepartmentName);
            mIndexTextView = (TextView) itemView.findViewById(R.id.tvDepartmentCode);
            mLevelTextView = (TextView) itemView.findViewById(R.id.tvCollegeOrSchool);
        }

        @Override
        public void onClick(View view) {
//            Intent intent = new Intent(getActivity(), StudentsActivity.class);
            Intent intent = StudentsActivity.newIntent(getContext(), mStudent.getStudentsId());
            startActivity(intent);
        }
        public  void bind(Student student){
            mStudent = student;
            mFullNameTextView.setText(student.getFirstName() + " " + student.getLastName());
            mIndexTextView.setText(student.getStudentsId().replace("_", "/"));
            mLevelTextView.setText(student.getLevel());
        }
    }

    private class StudentsAdapter extends RecyclerView.Adapter<StudentsHolder>{
        List<Student> mStudentList;

        public StudentsAdapter(List<Student> students){
            mStudentList = students;
        }
        @NonNull
        @Override
        public StudentsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new StudentsHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull StudentsHolder studentsHolder, int i) {
                Student student = mStudentList.get(i);
                studentsHolder.bind(student);
        }

        @Override
        public int getItemCount() {
            return mStudentList.size();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        init();
    }
    private void init(){
//        StudentsFactory studentsFactory = StudentsFactory.get(getActivity(), "student");
        final List<Student> students = new ArrayList<>();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot values : dataSnapshot.getChildren()){
                    Student student = (Student) values.getValue(Student.class);
                    students.add(student);
                }
                if (mStudentsAdapter == null){
                    mStudentsAdapter = new StudentsAdapter(students);
                    mSudentsRecyclerView.setAdapter(mStudentsAdapter);
                } else {
                    mStudentsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.students_list_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_student:
                Intent intent = new Intent(getActivity(), StudentsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
