package com.android.uccapp;

import java.util.Date;
import java.util.Map;

public class Student {
    private String mStudentsId;
    private String mFirstName;
    private String mLastName;
    private String mDepartment;
    private String mProgramme;
    private String mMajor;
    private Map<String, String> mCourses;
    private String mGender;
    private Date mDateOfBirth;
    private String mLevel;
    private String mPhone;
    private String mEmailAddress;

    public Student() {
        mDateOfBirth = new Date();
    }

    public Student(String firstName, String lastName, String department, String programme, String major, String gender, Date dateOfBirth, String level, String phone, String emailAddress, Map<String, String> courses) {
        mFirstName = firstName;
        mLastName = lastName;
        mProgramme = programme;
        mDepartment = department;
        mMajor = major;
        mGender = gender;
        mDateOfBirth = dateOfBirth;
        mLevel = level;
        mPhone = phone;
        mEmailAddress = emailAddress;
    }

    public String getStudentsId() {
        return mStudentsId;
    }

    public void setStudentsId(String studentsId) {
        mStudentsId = studentsId;
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

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }

    public String getProgramme() {
        return mProgramme;
    }

    public void setProgramme(String programme) {
        mProgramme = programme;
    }

    public String getMajor() {
        return mMajor;
    }

    public void setMajor(String major) {
        mMajor = major;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public Date getDateOfBirth() {
        return mDateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        mDateOfBirth = dateOfBirth;
    }

    public String getLevel() {
        return mLevel;
    }

    public void setLevel(String level) {
        mLevel = level;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getEmailAddresss() {
        return mEmailAddress;
    }

    public void setEmailAddresss(String emailAddresss) {
        mEmailAddress = emailAddresss;
    }

    public Map<String, String> getCourses() {
        return mCourses;
    }

    public void setCourses(Map<String, String> courses) {
        mCourses = courses;
    }

}
