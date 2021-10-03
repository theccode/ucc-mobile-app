package com.android.uccapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.uccapp.model.User;

import java.util.List;

public class UserListFragment extends Fragment {
    private RecyclerView mUsersRecyclerView;
    private UsersAdapter mUsersAdapter;
    private Toolbar mToolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUsersRecyclerView = (RecyclerView) view.findViewById(R.id.users_recycler);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private class UsersHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mFirstNameTextView;
        private TextView mLastNameTextView;
        private TextView mRegistrationTextView;
        private ImageView mUserProfileImageView;

        private User mUser;

        public UsersHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.user_list_item, parent, false));
            mFirstNameTextView = (TextView) itemView.findViewById(R.id.tvUserFirstname);
            mLastNameTextView = (TextView) itemView.findViewById(R.id.tvUserLastName);
            mRegistrationTextView = (TextView) itemView.findViewById(R.id.tvUserRegistrationNumber);
            mUserProfileImageView = (ImageView) itemView.findViewById(R.id.ivUserProfileImage);
            itemView.setOnClickListener(this);
        }

        public void bind(User user){
            mUser = user;
            mFirstNameTextView.setText(mUser.getFirstName());
            mLastNameTextView.setText(mUser.getLastName());
            mRegistrationTextView.setText(mUser.getRegistrationNumber().replace("_", "/"));
//            mUserProfileImageView.setImageDrawable();
        }
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "Go to User Details", Toast.LENGTH_LONG).show();
        }
    }
    private class UsersAdapter extends RecyclerView.Adapter<UsersHolder>{
        private List<User> mUsers;

        public UsersAdapter(List<User> users){
            mUsers = users;
        }
        @NonNull
        @Override
        public UsersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new UsersHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull UsersHolder usersHolder, int i) {
            usersHolder.bind(mUsers.get(i));
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }

        public void setUsers(List<User> users){
            mUsers = users;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        UserFactory userFactory = UserFactory.get(getContext());
        List<User> users = userFactory.getUsers();
        Log.d("USERS", users.toString());
        for (User user : users){
//            Log.d("USERS", user.getFirstName());
        }
        if (mUsersAdapter == null){
            mUsersAdapter = new UsersAdapter(users);
            mUsersRecyclerView.setAdapter(mUsersAdapter);
        } else {
            mUsersAdapter.setUsers(users);
            mUsersAdapter.notifyDataSetChanged();
        }
    }
}
