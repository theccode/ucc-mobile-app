package com.android.uccapp;

import android.content.Context;
import android.util.Log;

import com.android.uccapp.model.Student;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class StudentsFactory {
    private static StudentsFactory sStudentsFactory;
    private static   List<Student> mStudents;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static   List<Student> mStudentList;

    public static StudentsFactory get(Context context, String ref){
        if (sStudentsFactory == null){
            sStudentsFactory = new StudentsFactory(context);
        }
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference(ref);

        return sStudentsFactory;
    }

    private StudentsFactory(Context context){
        mStudents = new ArrayList<>();
        mStudentList = mStudents;
    }

    public  List<Student> getStudents(){

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Student student = dataSnapshot.getValue(Student.class);
                mStudentList.add(student);
                Log.d("LST", mStudentList.toString());
                getStudentList();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return mStudentList;
    }

    public Student getStudent(String index){
        for(Student student : mStudents){
            if (student.getStudentsId().equals(index)){
                return student;
            }
        }
        return null;
    }

    public  List<Student> getStudentList(){
        Log.d("ST",mStudentList.toString());
        for (Student student : mStudentList){
            Log.d("INDEX",student.getStudentsId());
        }
       return mStudentList;
    }

    public  void addStudent(Student student){
        mStudents.add(student);
    }

    public  void removeStudent(Student student){
        mStudents.remove(student);
    }
}
