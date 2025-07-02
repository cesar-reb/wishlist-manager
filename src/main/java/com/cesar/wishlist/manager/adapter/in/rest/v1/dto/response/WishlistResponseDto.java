package com.cesar.wishlist.manager.adapter.in.rest.v1.dto.response;

import com.cesar.wishlist.manager.domain.entity.Wishlist;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record WishlistResponseDto(List<String> products) {

    public static WishlistResponseDto fromWishlist(Wishlist wishlist) {
        return WishlistResponseDto.builder()
                .products(wishlist.getProducts().stream().toList())
                .build();
    }
}
