package com.cesar.wishlist.manager.application.service;

import com.cesar.wishlist.manager.application.config.WishlistProperties;
import com.cesar.wishlist.manager.domain.entity.Wishlist;
import com.cesar.wishlist.manager.domain.exception.WishlistMaxSizeException;
import com.cesar.wishlist.manager.domain.exception.WishlistNotFoundException;
import com.cesar.wishlist.manager.infra.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistServiceImplTest {

    @Mock
    private WishlistRepository repository;
    @Mock
    private WishlistProperties properties;
    @InjectMocks
    private WishlistServiceImpl service;

    private final String customerId = "customer123";
    private Collection<String> existingProducts = Set.of("product1", "product2");
    private Collection<String> newProducts;

    private Wishlist existingWishlist;
    private Wishlist updatedWishlist;

    @Nested
    @DisplayName("Given a wishlist exists")
    class GivenWishlistExists {

        @BeforeEach
        void setup() {
            givenExistingWishlistWithProducts();
        }

        @Test
        @DisplayName("When adding products within the limit")
        void shouldAddProductsToExistingWishlist() {
            givenWishlistIsFound();
            givenProductsSizeWithinLimit();
            givenWishlistSaveSuccessfully();

            whenAddingNewProductsSuccessfully();

            thenExpectWishlistUpdatedWithNewProducts();
            thenExpectRepositoryFindByIdCalledTimes(1);
            thenExpectRepositorySaveCalledTimes(1);
        }

        @Test
        @DisplayName("When adding products exceeding the limit")
        void shouldThrowWhenExceedingLimit() {
            givenWishlistIsFound();
            givenProductsSizeExceedLimit();

            whenAddingNewProductsThrowsWishlistMaxSizeException();

            thenExpectRepositoryFindByIdCalledTimes(1);
            thenExpectRepositorySaveCalledTimes(0);
        }

        @Test
        @DisplayName("When removing an existing product")
        void shouldRemoveProduct() {
            givenWishlistIsFound();
            givenWishlistSaveSuccessfully();

            whenRemovingAnExistingProductSuccessfully();

            thenExpectWishlistUpdatedWithoutProduct();
            thenExpectRepositoryFindByIdCalledTimes(1);
            thenExpectRepositorySaveCalledTimes(1);
        }

        @Test
        @DisplayName("When checking for an existing product")
        void shouldReturnTrueIfProductExists() {
            givenWishlistIsFound();

            boolean hasProduct = service.hasProduct(customerId, "product1");

            assertTrue(hasProduct);
            thenExpectRepositoryFindByIdCalledTimes(1);
        }

        @Test
        @DisplayName("When consulting all products in wishlist")
        void shouldReturnAllProducts() {
            givenWishlistIsFound();

            var products = whenConsultingAllProductsByCustomerSuccessfully();

            thenExpectAllProductsAreRetrieved(products);
            thenExpectRepositoryFindByIdCalledTimes(1);
        }

        // GIVEN METHODS
        private void givenWishlistIsFound() {
            when(repository.findById(customerId))
                    .thenReturn(Optional.of(existingWishlist));
        }

        private void givenExistingWishlistWithProducts() {
            existingWishlist = new Wishlist(customerId, existingProducts);
        }
        // WHEN METHODS
        private Wishlist whenConsultingAllProductsByCustomerSuccessfully() {
            return service.getAllProductsByCustomer(customerId);
        }
        private void whenRemovingAnExistingProductSuccessfully() {
            String productId = "product1";
            updatedWishlist = service.removeProduct(customerId, productId);
        }
        private void whenAddingNewProductsThrowsWishlistMaxSizeException() {
            newProducts = List.of("product3", "product4");
            assertThrows(WishlistMaxSizeException.class, () -> {
                updatedWishlist = service.addProducts(customerId, newProducts);
            });
        }
        // THEN METHODS

        private static void thenExpectAllProductsAreRetrieved(Wishlist wishlist) {
            assertEquals(Set.of("product1", "product2"), wishlist.getProducts());
        }

        private void thenExpectWishlistUpdatedWithoutProduct() {
            assertEquals(customerId, updatedWishlist.getCustomerId());
            assertFalse(updatedWishlist.contains("product1"));
        }

        private void thenExpectWishlistUpdatedWithNewProducts() {

            var expectedProducts = Set.of("product1", "product2", "product3", "product4");
            assertEquals(customerId, updatedWishlist.getCustomerId());
            assertTrue(updatedWishlist.getProducts().containsAll(expectedProducts));
        }


    }

    @Nested
    @DisplayName("Given no wishlist exists")
    class GivenNoWishlistExists {

        @Test
        @DisplayName("When adding products creates a new wishlist")
        void shouldCreateNewWishlist() {
            givenNoWishlistIsFound();
            givenProductsSizeWithinLimit();
            givenWishlistSaveSuccessfully();

            whenAddingNewProductsSuccessfully();

            thenExpectWishlistCreatedWithNewProducts();
            thenExpectRepositoryFindByIdCalledTimes(1);
            thenExpectRepositorySaveCalledTimes(1);

        }

        @Test
        @DisplayName("When removing a product of non existent wishlist")
        void shouldThrowWhenRemovingFromMissingWishlist() {
            givenNoWishlistIsFound();

            whenRemovingFromWishlistThrowsWishlistNotFoundException();

            thenExpectRepositoryFindByIdCalledTimes(1);
        }

        @Test
        @DisplayName("When checking a product of non existent wishlist")
        void shouldThrowWhenCheckingMissingWishlist() {
            givenNoWishlistIsFound();

            whenCheckingProductInMissingWishlistThrowsWishlistNotFoundException();

            thenExpectRepositoryFindByIdCalledTimes(1);
        }

        @Test
        @DisplayName("When retrieving products of non existent wishlist")
        void shouldThrowWhenGettingAllFromMissingWishlist() {

            givenNoWishlistIsFound();

            whenConsultingAllProductsByCustomerThrowsWishlistNotFoundException();

            thenExpectRepositoryFindByIdCalledTimes(1);
        }

        // GIVEN METHODS
        private void givenNoWishlistIsFound() {
            when(repository.findById(customerId)).thenReturn(Optional.empty());
        }
        // WHEN METHODS
        private void whenRemovingFromWishlistThrowsWishlistNotFoundException() {

            assertThrows(WishlistNotFoundException.class, () -> {
                service.removeProduct(customerId, "123");
            });
        }
        private void whenCheckingProductInMissingWishlistThrowsWishlistNotFoundException() {
            assertThrows(WishlistNotFoundException.class, () -> {
                service.hasProduct(customerId, "product1");
            });
        }
        private void whenConsultingAllProductsByCustomerThrowsWishlistNotFoundException() {
            assertThrows(WishlistNotFoundException.class, () -> {
                service.getAllProductsByCustomer(customerId);
            });
        }
        // THEN METHODS
        private void thenExpectWishlistCreatedWithNewProducts() {
            assertEquals(customerId, updatedWishlist.getCustomerId());
            assertTrue(updatedWishlist.getProducts().containsAll(newProducts));
            assertEquals(newProducts.size(), updatedWishlist.getProducts().size());
        }
    }

    // GIVEN METHODS
    private void givenWishlistSaveSuccessfully() {
        when(repository.save(any(Wishlist.class)))
                .thenAnswer(i -> i.getArgument(0));
    }
    private void givenProductsSizeWithinLimit() {
        when(properties.getMaxProducts()).thenReturn(20);
    }
    private void givenProductsSizeExceedLimit() {
        when(properties.getMaxProducts()).thenReturn(3);
    }

    // WHEN METHODS
    private void whenAddingNewProductsSuccessfully() {
        newProducts = List.of("product3", "product4");
        assertDoesNotThrow(() -> {
            updatedWishlist = service.addProducts(customerId, newProducts);
        });
    }

    // THEN METHODS

    private void thenExpectRepositorySaveCalledTimes(int desiredTimes) {
        verify(repository, times(desiredTimes)).save(updatedWishlist);
    }

    private void thenExpectRepositoryFindByIdCalledTimes(int desiredTimes) {
        verify(repository, times(desiredTimes)).findById(customerId);
    }
}