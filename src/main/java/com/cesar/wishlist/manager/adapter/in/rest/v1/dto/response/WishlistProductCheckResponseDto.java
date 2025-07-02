package com.cesar.wishlist.manager.adapter.in.rest.v1.dto.response;

import lombok.Builder;

@Builder
public record WishlistProductCheckResponseDto(boolean exists) {
}
