package com.example.duanthutap.model;

public class Oder {
    private String id;
    private String id_user;
    private String name;
    private String image;
    private Double total;
    private String date;
    private String address;
    private String phone_number;
    private String status;

    public Oder() {
    }

    public Oder(String id, String id_user, String name, String image, Double total, String date, String address, String phone_number, String status) {
        this.id = id;
        this.id_user = id_user;
        this.name = name;
        this.image = image;
        this.total = total;
        this.date = date;
        this.address = address;
        this.phone_number = phone_number;
        this.status = status;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
