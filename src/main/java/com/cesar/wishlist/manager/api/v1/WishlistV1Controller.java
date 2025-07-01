package com.cesar.wishlist.manager.api.v1;

import com.cesar.wishlist.manager.api.v1.dto.WishlistAddProductsDto;
import com.cesar.wishlist.manager.api.v1.dto.WishlistProductCheckResponseDto;
import com.cesar.wishlist.manager.api.v1.dto.WishlistResponseDto;
import com.cesar.wishlist.manager.application.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/wishlists")
@RequiredArgsConstructor
public class WishlistV1Controller implements WishlistV1Api {

    private final WishlistService service;

    public ResponseEntity<WishlistResponseDto> addProducts(String customerId, WishlistAddProductsDto products) {

        var wishlist = service.addProducts(customerId, products.products());
        return ResponseEntity.ok(WishlistResponseDto.fromWishlist(wishlist));
    }

    public ResponseEntity<WishlistResponseDto> removeProduct(String customerId, String productId) {

        var wishlist = service.removeProduct(customerId, productId);
        return ResponseEntity.ok(WishlistResponseDto.fromWishlist(wishlist));
    }

    public ResponseEntity<WishlistProductCheckResponseDto> hasProduct(String customerId, String productId) {

        boolean exists = service.hasProduct(customerId, productId);
        return ResponseEntity.ok(new WishlistProductCheckResponseDto(exists));
    }

    public ResponseEntity<WishlistResponseDto> getAllProductsByCustomer(String customerId) {

        var wishlist = service.getAllProductsByCustomer(customerId);
        return ResponseEntity.ok(WishlistResponseDto.fromWishlist(wishlist));
    }
}
