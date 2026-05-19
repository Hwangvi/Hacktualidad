package com.Hacktualidad.repository;

import com.Hacktualidad.entity.Cart;
import com.Hacktualidad.entity.CartElement;
import com.Hacktualidad.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartElementRepository extends JpaRepository<CartElement, Long> {
    List<CartElement> findByCart(Cart cart);
    Optional<CartElement> findByCartAndProduct(Cart cart, Product product);
    void deleteByCart(Cart cart);
}