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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Staff;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StaffListFragment extends Fragment {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private StaffAdapter mStaffAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("staff", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_list, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("Please insert user role!");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.staff_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateUI();

        return view;
    }
    private class StaffHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Staff mStaff;
        private ImageView mImageView;
        private TextView mFullNameTextView;
        private TextView mStaffIdTextView;
        private TextView mStaffsRole;

        public StaffHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.fragment_staff_list_person, parent, false));
            mImageView = (ImageView) itemView.findViewById(R.id.ivCourseIcon);
            mFullNameTextView = (TextView) itemView.findViewById(R.id.tvProgram);
            mStaffIdTextView = (TextView) itemView.findViewById(R.id.tvFeeAmount);
            mStaffsRole = (TextView) itemView.findViewById(R.id.tvLevel);
            itemView.setOnClickListener(this);
        }

        public void bind(Staff staff){
            mStaff = staff;
            mFullNameTextView.setText(mStaff.getFirstName() + " " + mStaff.getLastName());
            mStaffIdTextView.setText(mStaff.getStaffId());
            mStaffsRole.setText(mStaff.isAdmin() ? "Administrator" : "Lecturer");
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getContext(), StaffActivity.class);
            startActivity(intent);
        }
    }
    private class StaffAdapter extends RecyclerView.Adapter<StaffHolder>{
        List<Staff> mStaffs;

        public StaffAdapter(List<Staff> staffs){
            mStaffs = staffs;
        }
        @NonNull
        @Override
        public StaffHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new StaffHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull StaffHolder staffHolder, int i) {
            staffHolder.bind(mStaffs.get(i));
        }

        @Override
        public int getItemCount() {
            return mStaffs.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){


        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Staff> staffs = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    new Staff();
                    Staff staff;
                    staff = (Staff) data.getValue(Staff.class);
                    staffs.add(staff);
                }
                if (mStaffAdapter == null){
                    mStaffAdapter = new StaffAdapter(staffs);
                    mRecyclerView.setAdapter(mStaffAdapter);
                } else {
                    mStaffAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
