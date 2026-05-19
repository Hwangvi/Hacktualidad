package com.Hacktualidad.repository;

import com.Hacktualidad.entity.Cart;
import com.Hacktualidad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}