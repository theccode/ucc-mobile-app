package com.android.uccapp.database;

public class UserDBSchema {
    public static final class UsersTable {
        public static final String NAME = "users";
        public static final class Cols {
            public static final String REGISTRATION_NUMBER = "registration_number";
            public static final String FIRSTNAME = "firstName";
            public static final String LASTNAME = "lastName";
            public static final String PASSWORD = "password";
            public static final String ISADMIN = "admin";
        }
    }
}
