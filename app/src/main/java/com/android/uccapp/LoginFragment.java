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

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.User;
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
//        mIndexNumberEditText.setText("PS/CSC/17/0060"); mPasswordEditText.setText("8cff7c3f-d22");
        mIndexNumberEditText.setText("PS/CSC/17/0006"); mPasswordEditText.setText("80ec76f4-9a6");
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final String index = mIndexNumberEditText.getText().toString().replace("/", "_").toUpperCase();
                final String password = mPasswordEditText.getText().toString();

                Log.d("INDEX", index);

//                String customToken = ConfigUtility.mFirebaseAuth.createCustomToken
                if (!index.isEmpty() && !password.isEmpty()){
//                    User user = UserFactory.get(getContext()).getUser(index);
                    ConfigUtility.createFirebaseUtil("users", getActivity());
                    DatabaseReference databaseReference = ConfigUtility.mFirebaseReference;
                    databaseReference.child(index).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            mUser = user;
                            if (null != mUser){
                                if (mUser.getPassword().equals(password)){
                                    if (mUser.isAdmin()) {
                                        Intent intent = AdministratorActivity.newIntent(getContext(), mUser);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        getActivity().finish();
                                    } else {
                                        Intent intent = MainDashboard.newIntent(getContext(), mUser);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }

                                } else {
                                    Toast.makeText(getContext(), "Incorrect Index or Password", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "User doesn't exist!", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    if (index.isEmpty()){
                        Toast.makeText(getContext(), "Registration Number field cannot be empty!", Toast.LENGTH_LONG).show();
                    }
                    else if (password.isEmpty()){
                        Toast.makeText(getContext(), "Password field cannot be empty!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return view;
    }
}
