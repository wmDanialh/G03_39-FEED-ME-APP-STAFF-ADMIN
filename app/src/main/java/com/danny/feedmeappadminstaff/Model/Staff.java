package com.danny.feedmeappadminstaff.Model;

public class Staff {

    public String staffMobile;
    public String staffName;
    public String staffEmail;
    public String staffAddress;
    public String uid;

    public Staff() {
    }

    public Staff(String staffAddress,String staffEmail, String staffMobile, String staffName , String uid) {
        this.staffAddress = staffAddress;
        this.staffEmail = staffEmail;
        this.staffMobile = staffMobile;
        this.staffName = staffName;
        this.uid = uid;
    }

    public String getStaffMobile() {
        return staffMobile;
    }

    public void setStaffMobile(String staffMobile) {
        this.staffMobile = staffMobile;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }

    public String getStaffAddress() {
        return staffAddress;
    }

    public void setStaffAddress(String staffAddress) {
        this.staffAddress = staffAddress;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}