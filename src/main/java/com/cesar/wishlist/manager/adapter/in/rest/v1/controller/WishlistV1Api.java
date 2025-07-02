package com.cesar.wishlist.manager.adapter.in.rest.v1.controller;

import com.cesar.wishlist.manager.adapter.in.rest.v1.dto.request.WishlistAddProductsDto;
import com.cesar.wishlist.manager.adapter.in.rest.v1.dto.response.WishlistProductCheckResponseDto;
import com.cesar.wishlist.manager.adapter.in.rest.v1.dto.response.WishlistResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Wishlist API v1", description = "Gerenciamento da wishlist do cliente")
@RequestMapping("/v1/wishlists")
public interface WishlistV1Api {

    @Operation(summary = "Adiciona produtos à wishlist de um cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos adicionados com sucesso"),
            @ApiResponse(responseCode = "422", description = "Limite máximo de produtos excedido")
    })
    @PostMapping("/products")
    ResponseEntity<WishlistResponseDto> addProducts(
            @Parameter(in = ParameterIn.HEADER, description = "ID do cliente", required = true, example = "1234")
            @RequestHeader String customerId,
            @RequestBody @Valid WishlistAddProductsDto products);

    @Operation(summary = "Remove um produto da wishlist do cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Wishlist não encontrada")
    })
    @DeleteMapping("/products/{productId}")
    ResponseEntity<WishlistResponseDto> removeProduct(
            @Parameter(in = ParameterIn.HEADER, description = "ID do cliente", required = true)
            @RequestHeader String customerId,
            @Parameter(description = "ID do produto a ser removido") @PathVariable String productId);

    @Operation(summary = "Verifica se um produto está na wishlist")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado da verificação"),
            @ApiResponse(responseCode = "404", description = "Wishlist não encontrada")
    })
    @GetMapping("/products/{productId}/exists")
    ResponseEntity<WishlistProductCheckResponseDto> hasProduct(
            @Parameter(in = ParameterIn.HEADER, description = "ID do cliente", required = true)
            @RequestHeader String customerId,
            @Parameter(description = "ID do produto") @PathVariable String productId);

    @Operation(summary = "Retorna todos os produtos da wishlist de um cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produtos retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Wishlist não encontrada")
    })
    @GetMapping("/products")
    ResponseEntity<WishlistResponseDto> getAllProductsByCustomer(
            @Parameter(in = ParameterIn.HEADER, description = "ID do cliente", required = true)
            @RequestHeader String customerId);
}