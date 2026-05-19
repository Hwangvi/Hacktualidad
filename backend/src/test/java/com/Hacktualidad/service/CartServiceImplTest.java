package com.Hacktualidad.service;

import com.Hacktualidad.dto.CartDTO;
import com.Hacktualidad.dto.CartElementDTO;
import com.Hacktualidad.entity.*;
import com.Hacktualidad.mapper.CartMapper;
import com.Hacktualidad.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock private CartRepository cartRepository;
    @Mock private CartElementRepository cartElementRepository;
    @Mock private UserRepository userRepository;
    @Mock private ProductRepository productRepository;
    @Mock private CartMapper cartMapper;
    @Mock private EmailService emailService;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void shouldAddProductToCart() {
        String email = "user@test.com";
        User user = new User();
        Cart cart = new Cart();
        Product product = new Product();
        product.setStock(10);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartElementRepository.findByCartAndProduct(cart, product)).thenReturn(Optional.empty());

        when(cartElementRepository.findByCart(cart)).thenReturn(new ArrayList<>());
        when(cartMapper.toCartElementDTOList(any())).thenReturn(new ArrayList<>());

        cartService.addProductToCart(email, 1L);

        verify(cartElementRepository).save(any(CartElement.class));
    }

    @Test
    void shouldThrowErrorIfOutOfStock() {
        String email = "user@test.com";
        Product product = new Product();
        product.setStock(0);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));
        when(cartRepository.findByUser(any())).thenReturn(Optional.of(new Cart()));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class, () -> cartService.addProductToCart(email, 1L));
    }

    @Test
    void shouldCheckoutCorrectly() {
        String email = "user@test.com";
        User user = new User();
        user.setUserId(1L);

        Product product = new Product();
        product.setProductId(100L);
        product.setStock(10);

        CartElement item = new CartElement();
        item.setProduct(product);
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        List<CartElement> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        when(cartElementRepository.findByCart(cart)).thenReturn(items);

        when(cartMapper.toCartElementDTOList(items)).thenReturn(new ArrayList<>());

        cartService.checkout(email);

        assertEquals(8, product.getStock());
        verify(productRepository, atLeastOnce()).save(product);
        verify(emailService).sendOrderConfirmation(eq(email), any());

        assertTrue(cart.getItems().isEmpty());
    }
}