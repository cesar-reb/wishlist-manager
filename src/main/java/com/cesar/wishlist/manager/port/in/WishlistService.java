package com.cesar.wishlist.manager.port.in;

import com.cesar.wishlist.manager.domain.entity.Wishlist;

import java.util.Collection;

public interface WishlistService {

    Wishlist addProducts(String customerId, Collection<String> products);
    Wishlist removeProduct(String customerId, String productId);
    boolean hasProduct(String customerId, String productId);
    Wishlist getAllProductsByCustomer(String customerId);
}
