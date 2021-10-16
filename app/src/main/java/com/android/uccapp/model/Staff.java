package com.android.uccapp.model;

import java.io.Serializable;

public class Staff implements Serializable {
    private String mFirstName;
    private String mLastName;
    private String mStaffId;
    private String mCourse;
    private String mEmailAddress;
    private String mPhoneNumber;
    private boolean mIsAdmin;
    private boolean mIsLecturer;

    public Staff() {
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getStaffId() {
        return mStaffId;
    }

    public void setStaffId(String staffId) {
        mStaffId = staffId;
    }

    public String getCourse() {
        return mCourse;
    }

    public void setCourse(String course) {
        mCourse = course;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    public void setAdmin(boolean admin) {
        mIsAdmin = admin;
    }

    public boolean isLecturer() {
        return mIsLecturer;
    }

    public void setLecturer(boolean lecturer) {
        mIsLecturer = lecturer;
    }
}
