package com.cesar.wishlist.manager.api.v1.dto;

import lombok.Builder;

@Builder
public record WishlistProductCheckResponseDto(boolean exists) {
}
