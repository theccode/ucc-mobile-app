package com.android.uccapp.model;

public class RegisteredCourses {
    private String courseTitle;
    private String creditHours;

    public RegisteredCourses() {
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(String creditHours) {
        this.creditHours = creditHours;
    }
}
