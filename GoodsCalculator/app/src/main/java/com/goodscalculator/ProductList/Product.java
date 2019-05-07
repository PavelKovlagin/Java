package com.goodscalculator.ProductList;

public class Product {

    Product() {

    }

    Product (int id_product, String bar_code, String name, double price) {
        this.id_product = id_product;
        this.bar_code = bar_code;
        this.name = name;
    }

    private int id_product;
    private String bar_code;
    private String name;
    private double price;

    public int getId() {
        return id_product;
    }

    public void setId(int id) {
        this.id_product = id;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = this.price + price;
    }

    @Override
    public String toString() {
        return this.id_product + " " + this.bar_code + " " + this.name + " " + this.price;
    }
}
