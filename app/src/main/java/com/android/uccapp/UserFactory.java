package com.android.uccapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.uccapp.database.UserBaseHelper;
import com.android.uccapp.database.UserDBSchema.UsersTable;
import com.android.uccapp.database.UsersCursorWrapper;
import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.FIRSTNAME;
import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.ISADMIN;
import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.LASTNAME;
import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.PASSWORD;
import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.REGISTRATION_NUMBER;

public class UserFactory {
    private static UserFactory cUserFactory;
    private List<User> mUsers;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;
    private User mUser;
    private Activity caller;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;

    public static UserFactory get(Context context){
        if (cUserFactory == null){
            cUserFactory = new UserFactory(context);
        }
        return cUserFactory;
    }

    public  DatabaseReference getUserReference(String ref, Activity lActivity){
        caller = lActivity;
        ConfigUtility.createFirebaseUtil(ref, caller);
        mFirebaseDatabase = ConfigUtility.mFirebaseDatabase;
        mDatabaseReference = ConfigUtility.mFirebaseReference;
        return mDatabaseReference;
    }
    private UserFactory(Context context){
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new UserBaseHelper(mContext).getWritableDatabase();
        mUsers = new ArrayList<>();
    }

    public void addUser(User user){
        ContentValues values = getContentValues(user);
        mSQLiteDatabase.insert(UsersTable.NAME, null, values);
    }
    private void updateUser(User user){
        ContentValues values = getContentValues(user);
        String regString = user.getRegistrationNumber();
        mSQLiteDatabase.update(UsersTable.NAME, values, REGISTRATION_NUMBER + " = ?", new String[]{regString});
    }
    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        UsersCursorWrapper cursor = queryUsers(null, null);
        try {
            while (cursor.moveToNext()){
                cursor.moveToFirst();
                users.add(cursor.getUser());
            }
        } finally {
            cursor.close();
        }

        return users;
    }
    public User getUser(String registrationNumber){
        UsersCursorWrapper cursor = queryUsers(REGISTRATION_NUMBER + " = ?", new String[]{registrationNumber});
        if (cursor.getCount() == 0){
            return null;
        }
        try{
            cursor.moveToFirst();
            return cursor.getUser();
        } finally {
            cursor.close();
        }
    }
    public void removeUser(User user){

    }

    //DATABASE OPERATIONS
    private ContentValues getContentValues (User user){
        ContentValues values = new ContentValues();
        values.put(REGISTRATION_NUMBER, user.getRegistrationNumber());
        values.put(FIRSTNAME, user.getFirstName());
        values.put(LASTNAME, user.getLastName());
        values.put(PASSWORD, user.getPassword());
        values.put(ISADMIN, user.isAdmin());
        return values;
    }

    private UsersCursorWrapper queryUsers(String whereClause, String[] whereArgs){
        Cursor cursor = mSQLiteDatabase.query(
                UsersTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new UsersCursorWrapper(cursor);
    }
}
