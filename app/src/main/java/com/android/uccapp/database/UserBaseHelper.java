package com.android.uccapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.uccapp.database.UserDBSchema.UsersTable.Cols;

import static com.android.uccapp.database.UserDBSchema.*;
import static com.android.uccapp.database.UserDBSchema.UsersTable.Cols.*;
import static com.android.uccapp.database.UserDBSchema.UsersTable.NAME;
public class UserBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "users.db";
    public UserBaseHelper( Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + NAME +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT " + ", "
                + REGISTRATION_NUMBER+ ", "
                + FIRSTNAME + ", "
                + LASTNAME + ", "
                + PASSWORD + ", "
                + ISADMIN +
                ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
