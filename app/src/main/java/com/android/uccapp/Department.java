package com.android.uccapp;

public class Department {
    private String mDepartmentCode;
    private String mDepartmentName;
    private String mCollegeOrSchool;

    public Department() {
    }

    public String getDepartmentCode() {
        return mDepartmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        mDepartmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return mDepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        mDepartmentName = departmentName;
    }

    public String getCollegeOrSchool() {
        return mCollegeOrSchool;
    }

    public void setCollegeOrSchool(String collegeOrSchool) {
        mCollegeOrSchool = collegeOrSchool;
    }
}
