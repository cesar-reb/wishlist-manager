package com.cesar.wishlist.manager.application.service;

import com.cesar.wishlist.manager.application.config.WishlistProperties;
import com.cesar.wishlist.manager.domain.entity.Wishlist;
import com.cesar.wishlist.manager.domain.exception.WishlistMaxSizeException;
import com.cesar.wishlist.manager.domain.exception.WishlistNotFoundException;
import com.cesar.wishlist.manager.infra.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository repository;

    private final WishlistProperties wishlistProperties;

    @Override
    public Wishlist addProducts(String customerId, Collection<String> products) {

        log.info("Adding {} products to wishlist for customer: {}", products.size(), customerId);

        var wishlist = repository.findById(customerId)
                .map(wishlistFound -> {
                    log.info("Wishlist found for customer: {}", customerId);
                    ensureWishlistSizeRestriction(wishlistFound.getProducts().size(), products.size());
                    wishlistFound.addProducts(products);
                    return wishlistFound;
                })
                .orElseGet(createNewWishlist(customerId, products));

        return repository.save(wishlist);
    }

    @Override
    public void removeProduct(String customerId, String productId) {

        log.info("Removing product {} from wishlist for customer: {}", productId, customerId);

        repository.findById(customerId)
                .ifPresentOrElse(wishlist -> {
                            if (wishlist.contains(productId)) {
                                wishlist.removeProduct(productId);
                                repository.save(wishlist);
                            }
                        },
                        () -> {
                            log.warn("Wishlist not found for customer: {}", customerId);
                            throw new WishlistNotFoundException(customerId);
                        });
    }

    @Override
    public boolean hasProduct(String customerId, String productId) {

        log.info("Checking if product {} is in wishlist for customer: {}", productId, customerId);

        return repository.findById(customerId)
                .map(wishlist -> wishlist.contains(productId))
                .orElseThrow(() -> new WishlistNotFoundException(customerId));
    }

    @Override
    public Collection<String> getAllProductsByCustomer(String customerId) {

        log.info("Retrieving all products for customer: {}", customerId);

        return repository.findById(customerId)
                .map(Wishlist::getProducts)
                .orElseThrow(() -> new WishlistNotFoundException(customerId));
    }

    private Supplier<Wishlist> createNewWishlist(String customerId, Collection<String> products) {
        return () -> {
            log.info("Creating new wishlist for customer: {}", customerId);
            ensureWishlistSizeRestriction(0, products.size());
            return new Wishlist(customerId, products);
        };
    }

    private void ensureWishlistSizeRestriction(int currentSize, int productsToAdd) {
        if (currentSize + productsToAdd > wishlistProperties.getMaxProducts()) {
            throw new WishlistMaxSizeException(wishlistProperties.getMaxProducts());
        }
    }

}
