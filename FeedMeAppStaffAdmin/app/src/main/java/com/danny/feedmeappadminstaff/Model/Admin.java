package com.danny.feedmeappadminstaff.Model;

public class Admin {

    public String adminMobile;
    public String adminName;
    public String adminEmail;
    private String adminAddress;

    public Admin() {
    }

    public Admin(String adminMobile, String adminName, String adminEmail, String adminAddress) {
        this.adminMobile = adminMobile;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.adminAddress = adminAddress;
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public void setAdminMobile(String adminMobile) {
        this.adminMobile = adminMobile;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminAddress() {
        return adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
    }
}