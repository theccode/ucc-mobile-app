package com.android.uccapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Student;
import com.android.uccapp.model.StudentsFee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeeListFragment extends Fragment {
    private static final String ARG_FEE = "com.android.uccapp.fee";
    private RecyclerView mRecyclerView;
    private FeeAdapter mFeeAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    public static FeeListFragment newInstance(StudentsFee fee){
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_FEE, fee);
        FeeListFragment feeListFragment = new FeeListFragment();
        feeListFragment.setArguments(arg);
        return feeListFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("studentsFee", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fee_list, container, false);
        mRecyclerView = view.findViewById(R.id.fees_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateUI();
        return view;
    }
    private class FeeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mProgramTextView;
        private TextView mFeeAmountTextView;
        private TextView mLevelTextView;
        StudentsFee mFee;
        public  FeeHolder(LayoutInflater inflater, ViewGroup viewGroup){
            super(inflater.inflate(R.layout.fee_list_item, viewGroup, false));
            itemView.setOnClickListener(this);
            mProgramTextView = itemView.findViewById(R.id.tvProgram);
            mFeeAmountTextView = itemView.findViewById(R.id.tvFeeAmount);
            mLevelTextView = itemView.findViewById(R.id.tvLevel);
        }

        @Override
        public void onClick(View view) {
            Intent intent = FinancierActivity.newIntent(getContext(), mFee);
            startActivity(intent);
            Toast.makeText(getContext(), "CLICKED", Toast.LENGTH_LONG).show();
        }
        public void bind(StudentsFee fee){
            mFee = fee;
            mProgramTextView.setText(mFee.getProgram());
            mFeeAmountTextView.setText("Fee: "+mFee.getFeeAmount());
            mLevelTextView.setText("Level: " +mFee.getLevel());
        }
    }

    private class FeeAdapter extends RecyclerView.Adapter<FeeHolder>{
        private List<StudentsFee> mStudentsFeeList;

        public FeeAdapter(List<StudentsFee> studentsFeeList){
            mStudentsFeeList = studentsFeeList;
        }
        @NonNull
        @Override
        public FeeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            return new FeeHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull FeeHolder feeHolder, int i) {
                feeHolder.bind(mStudentsFeeList.get(i));
        }

        @Override
        public int getItemCount() {
            return mStudentsFeeList.size();
        }
    }
    private void updateUI(){
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<StudentsFee> studentsFeeList = new ArrayList<>();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    for(DataSnapshot datum: data.getChildren()){
                        StudentsFee studentsFee = datum.getValue(StudentsFee.class);
                        studentsFeeList.add(studentsFee);
                    }
                }
                if (mFeeAdapter == null){
                    mFeeAdapter = new FeeAdapter(studentsFeeList);
                    mRecyclerView.setAdapter(mFeeAdapter);
                } else {
                    mFeeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
