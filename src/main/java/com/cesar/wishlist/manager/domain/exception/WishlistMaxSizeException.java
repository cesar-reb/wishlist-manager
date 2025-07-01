package com.cesar.wishlist.manager.domain.exception;

public class WishlistMaxSizeException extends RuntimeException {

    public WishlistMaxSizeException(int maxSize) {
        super("Cannot add more than " + maxSize + " products to the wishlist.");
    }

    public WishlistMaxSizeException(String message) {
        super(message);
    }
}
