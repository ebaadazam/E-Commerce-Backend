package com.ebaad.ecommerce.model;

public class Size {
    private String name;
    private int quantity;

    // Constructor
    public Size() {
    }

    // Setters and Getters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
