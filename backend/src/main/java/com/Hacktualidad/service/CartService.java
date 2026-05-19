package com.Hacktualidad.service;

import com.Hacktualidad.dto.CartDTO;

public interface CartService {
    CartDTO getMyCart(String userEmail);
    CartDTO addProductToCart(String userEmail, Long productId);
    CartDTO removeProductFromCart(String userEmail, Long productId);
    void checkout(String userEmail);
}