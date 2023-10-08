package com.example.duanthutap.model;

public class ProductsAddCart {
    private String id;
    private String id_user;
    private String id_product;
    private String name_product;
    private String image_product;
    private int num_product;
    private double pricetotal_product;

    public ProductsAddCart() {
    }

    public ProductsAddCart(String id, String id_user, String id_product, String name_product, String image_product, int num_product, double pricetotal_product) {
        this.id = id;
        this.id_user = id_user;
        this.id_product = id_product;
        this.name_product = name_product;
        this.image_product = image_product;
        this.num_product = num_product;
        this.pricetotal_product = pricetotal_product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getImage_product() {
        return image_product;
    }

    public void setImage_product(String image_product) {
        this.image_product = image_product;
    }

    public int getNum_product() {
        return num_product;
    }

    public void setNum_product(int num_product) {
        this.num_product = num_product;
    }

    public double getPricetotal_product() {
        return pricetotal_product;
    }

    public void setPricetotal_product(double pricetotal_product) {
        this.pricetotal_product = pricetotal_product;
    }
}
