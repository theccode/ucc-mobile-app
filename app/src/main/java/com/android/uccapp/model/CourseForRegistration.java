package com.android.uccapp.model;

import java.util.List;

public class CourseForRegistration {
    private String courseCode;
    private String courseTitle;
    private String numOfCreditHours;

    public CourseForRegistration() {
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getNumOfCreditHours() {
        return numOfCreditHours;
    }

    public void setNumOfCreditHours(String numOfCreditHours) {
        this.numOfCreditHours = numOfCreditHours;
    }
}
