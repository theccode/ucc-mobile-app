package com.android.uccapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.uccapp.model.User;

public class UserFragment extends Fragment {
    private TextView mFirstNameTextView;
    private TextView mLastNameTextView;
    private TextView mRegistrationNumberTextView;
    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mUser = UserFactory.get(getContext()).getUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        mFirstNameTextView = (TextView) view.findViewById(R.id.tvUserFirstname);
        mLastNameTextView = (TextView) view.findViewById(R.id.tvUserLastName);
        mRegistrationNumberTextView = (TextView) view.findViewById(R.id.tvUserRegistrationNumber);
        return view;
    }
}
