package com.gftproject.shoppingcart.model;

public enum Status {
    DRAFT, SUBMITTED;

    public String getStatus() {

        return this.name();
    }
}
