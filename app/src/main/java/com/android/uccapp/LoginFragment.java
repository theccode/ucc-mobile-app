package com.android.uccapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {
    private EditText mIndexNumberEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConfigUtility.createFirebaseUtil("users", getActivity());
        mUser = ConfigUtility.mUser;
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        mFirebaseAuth = ConfigUtility.mFirebaseAuth;
        mAuthStateListener = ConfigUtility.mFirebaseAuthStateListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mIndexNumberEditText = view.findViewById(R.id.etIndex);
        mPasswordEditText = view.findViewById(R.id.etPassword);
        mLoginButton = view.findViewById(R.id.btnLogin);
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String index = mIndexNumberEditText.getText().toString().replace("/", "_");
                mDatabaseReference.orderByChild(index).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Intent showAdminDash = new Intent(getActivity(), AdministratorActivity.class);
                            startActivity(showAdminDash);
                            getActivity().finish();
                        } else if (!mIndexNumberEditText.getText().toString().isEmpty()) {
                            Intent showUserDash = new Intent(getActivity(), MainDashboard.class);
                            startActivity(showUserDash);
                            getActivity().finish();
                        }
                        Log.d("DATA", dataSnapshot.toString());
                        Log.d("INDEX", mIndexNumberEditText.getText().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getActivity(), "Signing in...", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
