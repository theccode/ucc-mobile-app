package com.android.uccapp.model;

import java.util.HashMap;
import java.util.List;

public class GradeBook {
    private List<HashMap<String, String>> courseCodesAndGrades;
    private String registrationNumber;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public List<HashMap<String, String>> getCourseCodesAndGrades() {
        return courseCodesAndGrades;
    }

    public void setCourseCodesAndGrades(List<HashMap<String, String>> courseCodesAndGrades) {
        this.courseCodesAndGrades = courseCodesAndGrades;
    }
}
