package com.cesar.wishlist.manager.application.service;

import com.cesar.wishlist.manager.domain.entity.Wishlist;

import java.util.Collection;

public interface WishlistService {

    Wishlist addProducts(String customerId, Collection<String> products);
    void removeProduct(String customerId, String productId);
    boolean hasProduct(String customerId, String productId);
    Collection<String> getAllProductsByCustomer(String customerId);
}
