package com.android.uccapp;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Staff;
import com.android.uccapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class StaffFragment extends Fragment {
    private Toolbar mToolbar;
    private EditText mStaffFirstName;
    private EditText mStaffLastName;
    private EditText mStaffID;
    private EditText mStaffCourse;
    private EditText mStaffEmail;
    private EditText mStaffPhone;
    private ImageView mStaffProfileImage;
    private ImageButton mStaffImageUploadButton;
    private CheckBox mAdminCheckBox;
    private CheckBox mLecturerCheckBox;
    private CheckBox mFinancierCheckBox;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Staff mStaff;
    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStaff = new Staff();
        mUser = new User();
        ConfigUtility.createFirebaseUtil("staff", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("Please insert user role!");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStaffFirstName = (EditText) view.findViewById(R.id.etFirstName);
        mStaffFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mStaff.setFirstName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mStaffLastName = (EditText) view.findViewById(R.id.etLastName);
        mStaffLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStaff.setLastName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mStaffID = (EditText) view.findViewById(R.id.etStaffId);
        mStaffID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStaff.setStaffId(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mStaffCourse = (EditText) view.findViewById(R.id.etCourse);
        mStaffCourse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStaff.setCourse(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mStaffEmail = (EditText) view.findViewById(R.id.etEmailAddress);
        mStaffEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStaff.setEmailAddress(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mStaffPhone = (EditText) view.findViewById(R.id.etPhone);
        mStaffPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStaff.setPhoneNumber(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mStaffProfileImage = (ImageView) view.findViewById(R.id.ivStaffImage);
        mStaffImageUploadButton = (ImageButton) view.findViewById(R.id.btnUpload);
        mStaffImageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Image to upload:TODO", Toast.LENGTH_LONG).show();
            }
        });
        mAdminCheckBox = (CheckBox) view.findViewById(R.id.cbAdmin);
        mAdminCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mStaff.setAdmin(b);
            }
        });
        mLecturerCheckBox = (CheckBox) view.findViewById(R.id.cbLecturer);
        mLecturerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mStaff.setLecturer(b);
            }
        });
        mFinancierCheckBox = (CheckBox) view.findViewById(R.id.cbFinancier);
        mFinancierCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mStaff.setFinancier(b);
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mStaff != null){
            if (mStaff.getStaffId() != null){
                if (mStaff.getStaffId() != null){
                    ConfigUtility.createFirebaseUtil("users", getActivity());
                    DatabaseReference usersDatabaseRef = ConfigUtility.mFirebaseReference;
                    usersDatabaseRef.child(mStaffID.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mUser = dataSnapshot.getValue(User.class);
                           if (null == mUser){
                               User user = new User();
                               user.setPassword(UUID.randomUUID().toString().substring(0, 12));
                               mUser = user;
                           }
                            mUser.setRegistrationNumber(mStaff.getStaffId());
                            mUser.setFirstName(mStaff.getFirstName());
                            mUser.setLastName(mStaff.getLastName());
                            mUser.setLecturer(mStaff.isLecturer());
                            mUser.setFinancier(mStaff.isFinancier());
                            UserFactory.get(getContext()).getUserReference("users", getActivity()).child(mStaff.getStaffId().replace("/", "_")).setValue(mUser);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mDatabaseReference.child(mStaff.getStaffId().replace("/", "_")).setValue(mStaff);
                }
            }
        }
    }
}
