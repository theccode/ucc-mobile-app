package com.android.uccapp.model;

import java.io.Serializable;
import java.util.Date;

public class StudentsFee implements Serializable {
    private String mDepartment;
    private String mProgram;
    private String mLevel;
    private String feeAmount;
    private Date mDate;

    public StudentsFee() {
        this.setDate(new Date());
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }

    public String getProgram() {
        return mProgram;
    }

    public void setProgram(String program) {
        mProgram = program;
    }

    public String getLevel() {
        return mLevel;
    }

    public void setLevel(String level) {
        mLevel = level;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
