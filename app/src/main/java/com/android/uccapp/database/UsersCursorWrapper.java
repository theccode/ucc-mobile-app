package com.android.uccapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.android.uccapp.model.User;

import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.FIRSTNAME;
import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.ISADMIN;
import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.LASTNAME;
import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.PASSWORD;
import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.REGISTRATION_NUMBER;

public class UsersCursorWrapper extends CursorWrapper {
    public UsersCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public User getUser(){
        String registrationNumber = getString(getColumnIndex(REGISTRATION_NUMBER));
        String firstName = getString(getColumnIndex(FIRSTNAME));
        String lastName = getString(getColumnIndex(LASTNAME));
        String password = getString(getColumnIndex(PASSWORD));
        int isAdmin = getInt(getColumnIndex(ISADMIN));

        User user = new User(registrationNumber);

        user.setRegistrationNumber(registrationNumber);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setAdmin(isAdmin != 0);

        return user;
    }
}
