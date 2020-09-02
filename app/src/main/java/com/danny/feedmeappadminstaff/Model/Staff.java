package com.danny.feedmeappadminstaff.Model;

public class Staff {

    private String staffImage;
    public String staffMobile;
    public String staffName;
    public String staffEmail;
    private String staffAddress;
    private String uid;

    public Staff() {
    }

    public Staff(String staffImage, String staffMobile, String staffName, String staffEmail, String staffAddress, String uid) {
        this.staffImage = staffImage;
        this.staffMobile = staffMobile;
        this.staffName = staffName;
        this.staffEmail = staffEmail;
        this.staffAddress = staffAddress;
        this.uid = uid;
    }

    public String getStaffImage() {
        return staffImage;
    }

    public void setStaffImage(String staffImage) {
        this.staffImage = staffImage;
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