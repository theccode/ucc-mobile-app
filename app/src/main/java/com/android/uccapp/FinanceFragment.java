package com.android.uccapp;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.Department;
import com.android.uccapp.model.Student;
import com.android.uccapp.model.StudentsFee;
import com.android.uccapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Base64;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FinanceFragment extends Fragment {
    private static final String ARG_USER = "com.android.uccapp.fee";
    private Toolbar mToolbar;
    private TextView mRegistrationNumberTextView;
    private TextView mExistingBalanceTextView;
    private TextView mFeeForAcademicYearTextView;
    private TextView mCurrentPaymentTextView;
    private TextView mTotalPaymentTextView;
    private TextView mBalanceTextView;
    private TextView mAcademicYearTextView;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private NumberFormat mNumberFormat;
    private User mUser;
    private Button mMakePaymentButton;

    public static FinanceFragment newInstance(User user){
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_USER, user);
        FinanceFragment financeFragment = new FinanceFragment();
        financeFragment.setArguments(arg);
        return financeFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("studentsFee", getActivity());
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mUser = (User) getArguments().getSerializable(ARG_USER);
        mNumberFormat = NumberFormat.getCurrencyInstance(new Locale("en", "Ghana"));
        DecimalFormatSymbols symbols = ((DecimalFormat) mNumberFormat).getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        ((DecimalFormat) mNumberFormat).setDecimalFormatSymbols(symbols);
        mNumberFormat.setMaximumFractionDigits(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, container, false);
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setTitle("Finance");
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init(view);
        return view;
    }

    private void init(View view) {
        mRegistrationNumberTextView = view.findViewById(R.id.tvUserRegistrationNumber);
        if (mUser != null){
            mRegistrationNumberTextView.setText(mUser.getRegistrationNumber().replace("_", "/"));
        }
        mExistingBalanceTextView = view.findViewById(R.id.tvExistingBalance);
        mFeeForAcademicYearTextView = view.findViewById(R.id.tvFeeForAcademicYear);
        ConfigUtility.createFirebaseUtil("departments", getActivity());
        DatabaseReference depRef = ConfigUtility.mFirebaseReference;
        depRef.child(mUser.getRegistrationNumber().split("_")[1]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
               ConfigUtility.createFirebaseUtil("student", getActivity());
               DatabaseReference studRef = ConfigUtility.mFirebaseReference;
               final Department department = dataSnapshot.getValue(Department.class);
               studRef.child(mUser.getRegistrationNumber()).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                      Student student = dataSnapshot.getValue(Student.class);
                       ConfigUtility.createFirebaseUtil("studentsFee", getActivity());
                       DatabaseReference feeRef = ConfigUtility.mFirebaseReference;
                       String depName = department.getDepartmentName().replaceAll("\\s+", "");
                       feeRef.child(depName).child(student.getLevel()).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               if (dataSnapshot.exists()){
                                   StudentsFee studentsFee = dataSnapshot.getValue(StudentsFee.class);
                                   if (studentsFee != null){
                                       mFeeForAcademicYearTextView.setText(mNumberFormat.format(Double.parseDouble(studentsFee.getFeeAmount())) + ".00");
                                   }
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
        mAcademicYearTextView = view.findViewById(R.id.tvAcademicYear);
        ConfigUtility.createFirebaseUtil("courseRegistrationOpening", getActivity());
        DatabaseReference academicRef = ConfigUtility.mFirebaseReference;
        academicRef.child("academicYear").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String academicYear = dataSnapshot.getValue(String.class);
                mAcademicYearTextView.setText("Fees for: " + academicYear);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mCurrentPaymentTextView = view.findViewById(R.id.tvPaymentMadeForThisYear);
        mTotalPaymentTextView = view.findViewById(R.id.tvTotalPayment);
        mBalanceTextView = view.findViewById(R.id.tvBalanceCarryForward);
        mMakePaymentButton = view.findViewById(R.id.btnMakePayment);
        mMakePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask(){

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        String token = null;
                        try {
                            token = generateToken();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d("TOKEN", token);
                        return null;
                    }
                }.execute();

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String generateToken() throws IOException {
        Base64.Encoder base64 = Base64.getEncoder();
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody requestBody = RequestBody.create(mediaType, "{\n \"providerCallbackHost\":\"localhost:8000\"}");
        Request request = new Request.Builder()
                .url("https://sandbox.momodeveloper.mtn.com/collection/token/")
                .post(requestBody)
                .addHeader("Ocp-Apim-Subscription-Key", "437450185aba4c3a9ceeca1a277664a2")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "cd8fe056-8933-4665-b003-73336a392322")
//                .addHeader("Authorization", String.valueOf(Base64.getEncoder("63c5fd5d-4a5f-41e7-abc0-dbf4fd13164a".getBytes(), "fc10dc262e2145369ec1a633fb237b30")))
                .build();

        Response response = client.newCall(request).execute();
        return response.toString();
    }
}
