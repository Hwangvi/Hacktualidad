package com.Hacktualidad.controller;

import com.Hacktualidad.dto.CartDTO;
import com.Hacktualidad.service.CartService;
// Importaciones de Swagger (OpenAPI)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "Carrito de Compras", description = "Operaciones para gestionar el carrito del usuario logueado")
public class CartRESTController {

    @Autowired
    private CartService cartService;

    private String getAuthEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Operation(summary = "Obtener mi carrito", description = "Recupera el carrito activo del usuario autenticado.")
    @ApiResponse(responseCode = "200", description = "Carrito recuperado correctamente")
    @GetMapping
    public ResponseEntity<CartDTO> getCart() {
        return ResponseEntity.ok(cartService.getMyCart(getAuthEmail()));
    }

    @Operation(summary = "Añadir producto", description = "Añade una unidad de un producto específico al carrito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto añadido"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PostMapping("/add/{productId}")
    public ResponseEntity<CartDTO> addToCart(
            @Parameter(description = "ID del producto a añadir", example = "10")
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.addProductToCart(getAuthEmail(), productId));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del carrito.")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartDTO> removeFromCart(
            @Parameter(description = "ID del producto a eliminar", example = "10")
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(getAuthEmail(), productId));
    }

    @Operation(summary = "Realizar Checkout", description = "Finaliza la compra y vacía el carrito.")
    @ApiResponse(responseCode = "200", description = "Compra realizada con éxito")
    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout() {
        cartService.checkout(getAuthEmail());
        return ResponseEntity.ok().build();
    }
}