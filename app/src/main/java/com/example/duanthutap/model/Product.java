package com.example.duanthutap.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;
@IgnoreExtraProperties
public class Product {
    private String id;
    private String name;
    private String category;
    private String description;
    private String img;
    private String color;
    private String size;
    private Double price;
    private int sold;
    private int quantity;
    private int num;

    public Product() {
    }

    public Product(String id,String name,String category, String description, String img, String color,String size, Double price,int sold,int quantity) {
        this.id = id;
        this.name=name;
        this.category = category;
        this.description = description;
        this.img = img;
        this.color = color;
        this.size=size;
        this.price = price;
        this.sold=sold;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("name",name);
        result.put("category",category);
        result.put("description",description);
        result.put("img",img);
        result.put("price",price);
        result.put("sold",sold);
        result.put("quantity",quantity);

        return result;
    }
}
