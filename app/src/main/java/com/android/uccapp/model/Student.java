package com.android.uccapp.model;

import java.util.Date;
import java.util.Map;

public class Student {
    private String mStudentsId;
    private String mPhotoUrl;
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
    private String mHallOfResidence;
    private String mRoomNumber;
    private String mCurrentResidenceAddress;
    private boolean mIsAdmin;

    public Student() {
        mDateOfBirth = new Date();
    }

    public Student(String firstName, String lastName, String department, String programme, String major, String gender, Date dateOfBirth, String level, String phone, String emailAddress, Map<String, String> courses, String photoUrl, boolean isAdmin) {
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
        mPhotoUrl = photoUrl;
        mIsAdmin = isAdmin;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
    }

    public String getHallOfResidence() {
        return mHallOfResidence;
    }

    public void setHallOfResidence(String hallOfResidence) {
        mHallOfResidence = hallOfResidence;
    }

    public String getRoomNumber() {
        return mRoomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        mRoomNumber = roomNumber;
    }

    public String getCurrentResidenceAddress() {
        return mCurrentResidenceAddress;
    }

    public void setCurrentResidenceAddress(String currentResidenceAddress) {
        mCurrentResidenceAddress = currentResidenceAddress;
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

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.mPhotoUrl = photoUrl;
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    public void setAdmin(boolean admin) {
        mIsAdmin = admin;
    }
}
