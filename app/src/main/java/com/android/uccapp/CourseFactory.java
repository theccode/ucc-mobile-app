package com.android.uccapp;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CourseFactory {
    private static  CourseFactory mCourseFactory;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;

    public static CourseFactory get(Context context, String ref){
        if (mCourseFactory == null){
            mCourseFactory = new CourseFactory();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
        }
        mDatabaseReference = mFirebaseDatabase.getReference(ref);
        return mCourseFactory;
    }
}
