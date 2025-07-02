package com.cesar.wishlist.manager.adapter.in.rest.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record WishlistAddProductsDto(@NotNull @Size(min = 1) List<String> products) {
}
