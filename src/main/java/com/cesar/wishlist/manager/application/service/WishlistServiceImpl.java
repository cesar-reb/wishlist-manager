package com.cesar.wishlist.manager.application.service;

import com.cesar.wishlist.manager.application.config.WishlistProperties;
import com.cesar.wishlist.manager.domain.entity.Wishlist;
import com.cesar.wishlist.manager.domain.exception.WishlistMaxSizeException;
import com.cesar.wishlist.manager.domain.exception.WishlistNotFoundException;
import com.cesar.wishlist.manager.infra.WishlistMongoRepository;
import com.cesar.wishlist.manager.port.in.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistMongoRepository repository;

    private final WishlistProperties wishlistProperties;

    @Override
    public Wishlist addProducts(String customerId, Collection<String> products) {

        log.info("Adding {} products to wishlist for customer: {}", products.size(), customerId);

        var wishlist = repository.findById(customerId)
                .map(updateWishlist(customerId, products))
                .orElseGet(createNewWishlist(customerId, products));

        return repository.save(wishlist);
    }

    @Override
    public Wishlist removeProduct(String customerId, String productId) {

        log.info("Removing product {} from wishlist for customer: {}", productId, customerId);

        return repository.findById(customerId)
                .map(removeProductFromWishlist(customerId, productId))
                .orElseThrow(() -> new WishlistNotFoundException(customerId));
    }

    @Override
    public boolean hasProduct(String customerId, String productId) {

        log.info("Checking if product {} is in wishlist for customer: {}", productId, customerId);

        return repository.findById(customerId)
                .map(wishlist -> wishlist.contains(productId))
                .orElseThrow(() -> new WishlistNotFoundException(customerId));
    }

    @Override
    public Wishlist getAllProductsByCustomer(String customerId) {

        log.info("Retrieving all products for customer: {}", customerId);

        return repository.findById(customerId)
                .orElseThrow(() -> new WishlistNotFoundException(customerId));
    }

    private Supplier<Wishlist> createNewWishlist(String customerId, Collection<String> products) {
        return () -> {
            log.info("Creating new wishlist for customer: {}", customerId);
            ensureWishlistSizeRestriction(0, products.size());
            return new Wishlist(customerId, products);
        };
    }

    private Function<Wishlist, Wishlist> updateWishlist(String customerId, Collection<String> products) {
        return wishlist -> {
            log.info("Updating Wishlist found for customer: {}", customerId);

            var productsToInsert = products.stream()
                    .filter( product -> !wishlist.contains(product))
                    .toList();

            ensureWishlistSizeRestriction(wishlist.getProducts().size(), productsToInsert.size());
            wishlist.addProducts(productsToInsert);
            return wishlist;
        };
    }

    private Function<Wishlist, Wishlist> removeProductFromWishlist(String customerId, String productId) {
        return wishlist -> {
            if (wishlist.contains(productId)) {
                wishlist.removeProduct(productId);
                return repository.save(wishlist);
            } else {
                log.info("Product {} was not in wishlist for customer: {}", productId, customerId);
                return wishlist;
            }
        };
    }

    private void ensureWishlistSizeRestriction(int currentSize, int productsToAdd) {
        if (currentSize + productsToAdd > wishlistProperties.getMaxProducts()) {
            throw new WishlistMaxSizeException(wishlistProperties.getMaxProducts());
        }
    }

}
