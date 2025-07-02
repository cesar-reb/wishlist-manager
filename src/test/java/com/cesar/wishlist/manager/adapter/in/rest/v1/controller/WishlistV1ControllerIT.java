package com.cesar.wishlist.manager.adapter.in.rest.v1.controller;

import com.cesar.wishlist.manager.adapter.in.rest.v1.dto.request.WishlistAddProductsDto;
import com.cesar.wishlist.manager.application.config.WishlistProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
@AutoConfigureMockMvc
class WishlistV1ControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WishlistProperties properties;

    private final String customerId = "integration-customer";

    private final String baseUrl = "/v1/wishlists/products";

    private String toJson(Object data) throws Exception {
        return objectMapper.writeValueAsString(data);
    }

    @Nested
    @DisplayName("POST /v1/wishlists/products")
    class AddProducts {

        @Test
        void shouldAddProductsSuccessfully() throws Exception {
            var request = new WishlistAddProductsDto(List.of("prod-1", "prod-2"));

            mockMvc.perform(post(baseUrl)
                            .header("customerId", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.products", hasSize(2)))
                    .andExpect(jsonPath("$.products", hasItems("prod-1", "prod-2")));
        }

        @Test
        void shouldIgnoreDuplicatesWhenAdding() throws Exception {
            var request = new WishlistAddProductsDto(List.of("dup", "dup"));

            mockMvc.perform(post(baseUrl)
                            .header("customerId", "newCustomerId")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.products", hasSize(1)))
                    .andExpect(jsonPath("$.products", hasItems("dup")));
        }

        @Test
        void shouldRejectWhenExceedingMaxProducts() throws Exception {
            var maxSize = properties.getMaxProducts() + 1;
            var tooManyProducts = new ArrayList<String>();
            for (int i = 0; i < maxSize; i++) tooManyProducts.add("p" + i);

            var request = new WishlistAddProductsDto(tooManyProducts);

            mockMvc.perform(post(baseUrl)
                            .header("customerId", customerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(toJson(request)))
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Nested
    @DisplayName("DELETE /v1/wishlists/products/{productId}")
    class RemoveProduct {

        @BeforeEach
        void setup() throws Exception {
            var request = new WishlistAddProductsDto(List.of("remove-me", "keep-me"));

            mockMvc.perform(post(baseUrl)
                    .header("customerId", customerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request)));
        }

        @Test
        void shouldRemoveProductSuccessfully() throws Exception {
            mockMvc.perform(delete(baseUrl + "/remove-me")
                            .header("customerId", customerId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.products", not(contains("remove-me"))))
                    .andExpect(jsonPath("$.products", hasItems("keep-me")));
        }

        @Test
        void shouldReturnNotFoundWhenWishlistDoesNotExist() throws Exception {
            mockMvc.perform(delete(baseUrl + "/remove-me")
                            .header("customerId", "non-existent"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /v1/wishlists/products/{productId}/exists")
    class HasProduct {

        private final String existsUrl = baseUrl + "/%s/exists";

        @BeforeEach
        void setup() throws Exception {
            var request = new WishlistAddProductsDto(List.of("prod-check"));

            mockMvc.perform(post(baseUrl)
                    .header("customerId", customerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request)));
        }

        @Test
        void shouldReturnTrueIfProductExists() throws Exception {
            mockMvc.perform(get(String.format(existsUrl, "prod-check"))
                            .header("customerId", customerId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exists").value("true"));
        }

        @Test
        void shouldReturnFalseIfProductDoesNotExist() throws Exception {
            mockMvc.perform(get(String.format(existsUrl, "non-existent-product"))
                            .header("customerId", customerId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exists").value("false"));
        }
    }

    @Nested
    @DisplayName("GET /v1/wishlists/products")
    class GetAllProducts {

        @BeforeEach
        void setup() throws Exception {
            var request = new WishlistAddProductsDto(List.of("p1", "p2", "p3"));

            mockMvc.perform(post(baseUrl)
                    .header("customerId", customerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request)));
        }

        @Test
        void shouldReturnAllProductsInWishlist() throws Exception {
            mockMvc.perform(get(baseUrl)
                            .header("customerId", customerId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.products", hasSize(3)));
        }
    }

}