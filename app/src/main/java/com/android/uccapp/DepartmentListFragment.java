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

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Department;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DepartmentListFragment extends Fragment {
    private RecyclerView mDepartmentRecyclerView;
    private Toolbar mToolbar;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private List<Department> mDepartments;
    private Department mDepartment;
    private DepartmentAdapter mDepartmentAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("departments", getActivity());
        mDepartments = new ArrayList<>();
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.department_recycler_view, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDepartment = new Department();
        setHasOptionsMenu(true);

        mDepartmentRecyclerView = (RecyclerView) view.findViewById(R.id.department_recycler);
        mDepartmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        init();
        return view;
    }

    private class DepartmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mDepartmentNameTextView;
        private TextView mDepartmentCodeTextView;
        private TextView mCollegeOrSchool;
        private Department lDepartment;

       public DepartmentHolder(LayoutInflater inflater, ViewGroup parent){
           super(inflater.inflate(R.layout.department_list_item, parent, false));
           mDepartmentNameTextView = (TextView) itemView.findViewById(R.id.tvDepartmentName);
           mDepartmentCodeTextView = (TextView) itemView.findViewById(R.id.tvDepartmentCode);
           mCollegeOrSchool = (TextView) itemView.findViewById(R.id.tvSemester);
           itemView.setOnClickListener(this);
       }

        public void bind(Department department){
            lDepartment = department;
            mDepartmentNameTextView.setText(lDepartment.getDepartmentName());
            mDepartmentCodeTextView.setText(lDepartment.getDepartmentCode());
            mCollegeOrSchool.setText(lDepartment.getCollegeOrSchool());
        }

        @Override
        public void onClick(View view) {
            Intent intent = DepartmentActivity.newIntent(getContext(), lDepartment.getDepartmentCode());
            startActivity(intent);
        }
    }
    private class DepartmentAdapter extends RecyclerView.Adapter<DepartmentHolder>{
        private List<Department> mDepartmentList;
        public DepartmentAdapter(List<Department> departments){
            mDepartmentList = departments;
        }
        @NonNull
        @Override
        public DepartmentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new DepartmentHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull DepartmentHolder departmentHolder, int i) {
            departmentHolder.bind(mDepartmentList.get(i));
        }

        @Override
        public int getItemCount() {
            return mDepartmentList.size();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        init();
    }

    private void init() {
        final List<Department> lDepartments = new ArrayList<>();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        Department department = data.getValue(Department.class);
                        lDepartments.add(department);
                    }
                    mDepartments = lDepartments;
                    if (mDepartmentAdapter == null){
                        mDepartmentAdapter = new DepartmentAdapter(mDepartments);
                        mDepartmentRecyclerView.setAdapter(mDepartmentAdapter);
                    } else {
                        mDepartmentAdapter.notifyDataSetChanged();
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
        inflater.inflate(R.menu.add_department_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_department:
                startActivity(new Intent(getActivity(), DepartmentActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
