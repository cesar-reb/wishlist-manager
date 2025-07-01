package com.cesar.wishlist.manager.domain.exception;

public class WishlistNotFoundException extends RuntimeException {

    public WishlistNotFoundException(String customerId) {
        super("Wishlist not found for customer ID: " + customerId);
    }

    public WishlistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}