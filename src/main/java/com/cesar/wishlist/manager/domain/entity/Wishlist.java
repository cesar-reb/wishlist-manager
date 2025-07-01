package com.cesar.wishlist.manager.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Wishlist {

    @Setter
    private String customerId;
    private Set<String> products = new HashSet<>();

    public Wishlist(String customerId, Collection<String> products) {
        this.customerId = customerId;
        this.addProducts(products);
    }

    public void addProducts(Collection<String> products) {
        this.products.addAll(products);
    }

    public void removeProduct(String productId) {
        this.products.remove(productId);
    }

    public boolean contains(String productId) {
        return this.products.contains(productId);
    }
}
