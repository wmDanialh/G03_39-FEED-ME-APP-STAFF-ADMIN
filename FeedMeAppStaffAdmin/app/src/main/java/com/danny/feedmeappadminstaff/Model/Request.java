package com.danny.feedmeappadminstaff.Model;

import java.util.List;

public class Request {

    private String name;
    private String phone;
    private String address;
    private String total;
    private String status;
    private String comment;
    private List<Order> foods; // list of food order

    public Request() {
    }

    public Request(String name, String phone, String address, String total, String status, String comment, List<Order> foods) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.status = "0";
        this.comment = comment;
        this.foods = foods;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}

