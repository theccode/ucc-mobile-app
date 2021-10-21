package com.android.uccapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;


import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Student;
import com.android.uccapp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class StudentsFragment extends Fragment {
    private static final int REQUEST_PHOTO = 42;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Student mStudent;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
    //Widgets
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mDepartmentEditText;
    private EditText mProgrammeEditText;
    private EditText mMajorEditText;
    private EditText mIndexNumberEditText;
    private Spinner mLevelSpinner;
    private Spinner mGenderSpinner;
    private Button mDOBButton;
    private ImageView mProfileImageView;
    private DatePicker mDOBDatePicker;
    private EditText mEmailEditText;
    private EditText mPhoneEditText;
    private CheckBox mAdminCheckbox;
    private ImageButton mImageButton;
    private EditText mHallOfResidenceEditText;
    private EditText mRoomNumberEditText;
    private EditText mCurrentPlaceOfResidenceEditText;
    private String[] mGender = new String[]{"Male", "Female"};
    private String[] mLevel = new String[]{"100", "200", "300", "400", "500", "600", "700", "800"};
    private ArrayAdapter<String> mGenderAdapter;
    private ArrayAdapter<String> mLevelAdapter;
    String indexNumber;
    private Toolbar mToolbar;
    private User mUser;

    //Constants
    private final String DIALOGUE_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final String ARG_STUDENT_ID = "com.android.uccapp.student_id";

    public static StudentsFragment newInstance(String studentsId){
        Bundle arg = new Bundle();
        arg.putString(ARG_STUDENT_ID, studentsId);
        StudentsFragment studentsFragment = new StudentsFragment();
        studentsFragment.setArguments(arg);
        return studentsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudent = new Student();
        mUser = new User();
        ConfigUtility.createFirebaseUtil("student", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        indexNumber = (String) getArguments().getSerializable(ARG_STUDENT_ID);
        mFirstNameEditText = view.findViewById(R.id.etFirstName);
        mProfileImageView = (ImageView) view.findViewById(R.id.ivStudentImage);
        mFirstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setFirstName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mLastNameEditText = view.findViewById(R.id.etLastName);
        mLastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setLastName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mDepartmentEditText = view.findViewById(R.id.etDepartment);
        mDepartmentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setDepartment(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mProgrammeEditText = view.findViewById(R.id.etCourse);
        mProgrammeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setProgramme(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mMajorEditText = view.findViewById(R.id.etMajor);
        mMajorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setMajor(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mIndexNumberEditText = view.findViewById(R.id.etStaffId);
        mIndexNumberEditText.setText(mStudent.getStudentsId());
        mIndexNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setStudentsId(charSequence.toString().replace("/", "_"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mGenderSpinner = view.findViewById(R.id.sGender);
        mGenderAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mGender);
        mGenderSpinner.setAdapter(mGenderAdapter);
        mLevelSpinner = view.findViewById(R.id.sLevel);
        mLevelAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mLevel);
        mLevelSpinner.setAdapter(mLevelAdapter);

        mDOBButton = view.findViewById(R.id.dobButton);
        mDOBButton.setText(new GregorianCalendar(1998, Calendar.MARCH, 22).getTime().toString());
        mDOBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mStudent.getDateOfBirth());
                datePickerFragment.setTargetFragment(StudentsFragment.this, REQUEST_DATE);
                datePickerFragment.show(fm, DIALOGUE_DATE);
            }
        });
        mPhoneEditText = view.findViewById(R.id.etPhone);
        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setPhone(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEmailEditText = view.findViewById(R.id.etEmailAddress);
        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setEmailAddresss(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mAdminCheckbox = (CheckBox) view.findViewById(R.id.cbAdmin);
        mAdminCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mUser.setAdmin(b);
                mStudent.setAdmin(b);
            }
        });
        mHallOfResidenceEditText = (EditText) view.findViewById(R.id.etHallOfResidence);
        mHallOfResidenceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mStudent.setHallOfResidence(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mRoomNumberEditText = (EditText) view.findViewById(R.id.etRoomNumber);
        mRoomNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setRoomNumber(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mCurrentPlaceOfResidenceEditText = (EditText) view.findViewById(R.id.etCurrentResidenceAddress);
        mCurrentPlaceOfResidenceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mStudent.setCurrentResidenceAddress(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (indexNumber != null){
            mDatabaseReference.child(indexNumber.replace("/", "_")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mStudent = dataSnapshot.getValue(Student.class);
                   if (mStudent != null){
                       mFirstNameEditText.setText(mStudent.getFirstName());
                       mLastNameEditText.setText(mStudent.getLastName());
                       mIndexNumberEditText.setText(mStudent.getStudentsId());
                       mDepartmentEditText.setText(mStudent.getDepartment());
                       mProgrammeEditText.setText(mStudent.getProgramme());
                       mMajorEditText.setText(mStudent.getMajor());
                       mPhoneEditText.setText(mStudent.getPhone());
                       mEmailEditText.setText(mStudent.getEmailAddresss());
                       mDOBButton.setText(mSimpleDateFormat.format(mStudent.getDateOfBirth()));
                       mAdminCheckbox.setChecked(mStudent.isAdmin());
                       mHallOfResidenceEditText.setText(mStudent.getHallOfResidence());
                       mRoomNumberEditText.setText(mStudent.getRoomNumber());
                       mCurrentPlaceOfResidenceEditText.setText(mStudent.getCurrentResidenceAddress());
                       mLevelSpinner.setSelection(getIndex(mLevelSpinner, mStudent.getLevel()));
                       mGenderSpinner.setSelection(getIndex(mGenderSpinner, mStudent.getGender()));
                       showImage(mStudent.getPhotoUrl());
                   }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        mImageButton = (ImageButton) view.findViewById(R.id.btnUpload);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Insert Picture"), REQUEST_PHOTO);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mStudent.setDateOfBirth(date);
            mDOBButton.setText(mStudent.getDateOfBirth().toString());
        } else if (requestCode == REQUEST_PHOTO){
            Uri imagePath = data.getData();
            StorageReference ref = ConfigUtility.mFirebaseStorageReference.child(imagePath.getLastPathSegment());
            ref.putFile(imagePath).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String imageUrl = taskSnapshot.getDownloadUrl().toString();
                    Log.d("URL", imageUrl);
                    mStudent.setPhotoUrl(imageUrl);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
       if (mStudent != null){
           mStudent.setGender(mGenderSpinner.getSelectedItem().toString());
           mStudent.setLevel(mLevelSpinner.getSelectedItem().toString());

            if (mStudent.getStudentsId() != null){
                mUser.setRegistrationNumber(mStudent.getStudentsId());
                mUser.setFirstName(mStudent.getFirstName());
                mUser.setLastName(mStudent.getLastName());
                mUser.setPhotoUrl(mStudent.getPhotoUrl());
                ConfigUtility.createFirebaseUtil("users", getActivity());
                DatabaseReference userRef = ConfigUtility.mFirebaseReference;
                userRef.child(mIndexNumberEditText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            mUser  = dataSnapshot.getValue(User.class);

                            if (mUser != null){
                                if (mUser.getPassword().isEmpty()){
                                    mUser.setPassword(UUID.randomUUID().toString().substring(0, 12));
                                }
                            }
                            UserFactory.get(getContext()).addUser(mUser);
                            mDatabaseReference.child(mStudent.getStudentsId()).setValue(mStudent);
                            UserFactory.get(getContext()).getUserReference("users", getActivity()).child(mUser.getRegistrationNumber()).setValue(mUser);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
       }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.remove_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.remove_student:
                mDatabaseReference.child(indexNumber.replace("/", "_")).removeValue();
                mStudent = null;
                Intent intent = new Intent(getActivity(), StudentsListActivity.class);
                startActivity(intent);
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showImage(String imagePath){
        if (imagePath != null && imagePath.isEmpty() == false){
            Picasso.with(getContext())
                    .load(imagePath)
                    .resize(50, 50)
                    .centerCrop()
                    .into(mProfileImageView);
        }
    }
    private int getIndex(Spinner spinner, String item){
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equals(item)){
                return i;
            }
        }
        return 0;
    }
}
