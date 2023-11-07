package com.example.duanthutap.model;

import java.io.Serializable;

public class Voucher implements Serializable {
    private String id;
    private String type;
    private String name;
    private String status;
    private String date;
    private String min;
    private String phanTram;

    private String img;
    private boolean isSelected;

    public Voucher() {
    }

    public Voucher(String id, String type, String name, String status, String date, String min, String phanTram, String img, boolean isSelected) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.date = date;
        this.min = min;
        this.phanTram = phanTram;
        this.img = img;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getPhanTram() {
        return phanTram;
    }

    public void setPhanTram(String phanTram) {
        this.phanTram = phanTram;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
