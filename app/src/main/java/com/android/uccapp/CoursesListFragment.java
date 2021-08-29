package com.android.uccapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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

public class CoursesListFragment extends Fragment {
    private Toolbar mToolbar;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private List<Course> mCourses;
    private Course mCourse;
    private CourseAdapter mCourseAdapter;
    private RecyclerView mCourseRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("courses", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mCourses = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.courses_recycler_view, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolbar(view);
        mCourse = new Course();
        setHasOptionsMenu(true);
        mCourseRecyclerView = (RecyclerView) view.findViewById(R.id.courses_recycler);
        mCourseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        init();
        return view;
    }

    private class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mCourseTitleTextView;
        private TextView mCourseCodeTextView;
        private TextView mLevelTextView;
        private TextView mSemesterTextView;
        private Course mCourse;
        public CourseHolder(LayoutInflater layoutInflater, ViewGroup parent){
            super(layoutInflater.inflate(R.layout.course_list_item, parent, false));
            mCourseTitleTextView = (TextView) itemView.findViewById(R.id.tvCourseName);
            mCourseCodeTextView = (TextView) itemView.findViewById(R.id.tvCourseCode);
            mLevelTextView = (TextView) itemView.findViewById(R.id.tvCourseLevel);
            mSemesterTextView = (TextView) itemView.findViewById(R.id.tvCourseSemester);
            itemView.setOnClickListener(this);
        }
        public CourseHolder(View itemView){
            super(itemView);
        }

        public void bind(Course course){
            mCourse = course;
            mCourseCodeTextView.setText(course.getCourseCode());
            mCourseTitleTextView.setText(course.getCourseName());
            mLevelTextView.setText("L"+course.getLevel());
            mSemesterTextView.setText(course.getSemester());
        }

        @Override
        public void onClick(View view) {
            Intent intent = CourseActivity.newIntent(getContext(), mCourse.getCourseCode());
            startActivity(intent);
        }
    }
    private  class CourseAdapter extends RecyclerView.Adapter<CourseHolder>{
        List<Course> lmCourses;

        public CourseAdapter(List<Course> courses){
            lmCourses = courses;
        }
        @NonNull
        @Override
        public CourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CourseHolder(layoutInflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseHolder courseHolder, int i) {
            courseHolder.bind(lmCourses.get(i));
        }

        @Override
        public int getItemCount() {
            return lmCourses.size();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        init();
    }

    public void init(){
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final List<Course> courses = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        Course course =  data.getValue(Course.class);
                        courses.add(course);
                    }
                    mCourses = courses;
                    if (mCourseAdapter == null){
                        mCourseAdapter = new CourseAdapter(courses);
                        mCourseRecyclerView.setAdapter(mCourseAdapter);
                    } else {
                        mCourseAdapter.notifyDataSetChanged();
                    }
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
        inflater.inflate(R.menu.new_course_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_course:
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                startActivity(intent);
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }
    private void initCollapsingToolbar(View view) {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appBarLayout);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

}
