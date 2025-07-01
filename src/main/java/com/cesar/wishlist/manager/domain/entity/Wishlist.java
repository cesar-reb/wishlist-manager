package com.cesar.wishlist.manager.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Getter
@Document(collection = "wishlists")
public class Wishlist {

    @Setter
    @Id
    private String customerId;

    private final Set<String> products = new HashSet<>();

    private final LocalDateTime createdAt;

    public Wishlist(String customerId, Collection<String> products) {
        this.customerId = customerId;
        this.addProducts(products);
        this.createdAt = LocalDateTime.now();
    }

    public void addProducts(Collection<String> products) {
        Optional.ofNullable(products)
                .ifPresent(pdcts -> pdcts.forEach(this::addProduct));
    }

    public void addProduct(String productId) {
        Objects.requireNonNull(productId, "Product ID must not be null");
        this.products.add(productId);
    }

    public void removeProduct(String productId) {
        this.products.remove(productId);
    }

    public boolean contains(String productId) {
        return this.products.contains(productId);
    }
}
