package com.Hacktualidad.service;

import com.Hacktualidad.dto.CartDTO;
import com.Hacktualidad.dto.CartElementDTO;
import com.Hacktualidad.entity.Cart;
import com.Hacktualidad.entity.CartElement;
import com.Hacktualidad.entity.Product;
import com.Hacktualidad.entity.User;
import com.Hacktualidad.mapper.CartMapper;
import com.Hacktualidad.repository.CartElementRepository;
import com.Hacktualidad.repository.CartRepository;
import com.Hacktualidad.repository.ProductRepository;
import com.Hacktualidad.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartElementRepository cartElementRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CartMapper cartMapper;
    @Autowired private EmailService emailService;

    private Cart getOrCreateCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + userEmail));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    private CartDTO buildCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());

        List<CartElement> elements = cartElementRepository.findByCart(cart);
        List<CartElementDTO> elementDTOs = cartMapper.toCartElementDTOList(elements);
        cartDTO.setItems(elementDTOs);

        double total = elementDTOs.stream()
                .mapToDouble(CartElementDTO::getSubtotal)
                .sum();
        cartDTO.setTotalAmount(total);

        return cartDTO;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "carts", key = "#userEmail")
    public CartDTO getMyCart(String userEmail) {
        Cart cart = getOrCreateCart(userEmail);
        return buildCartDTO(cart);
    }

    @Override
    @Transactional
    @CachePut(value="carts", key = "#userEmail")
    public CartDTO addProductToCart(String userEmail, Long productId) {
        Cart cart = getOrCreateCart(userEmail);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        if (product.getStock() <= 0) {
            throw new RuntimeException("ERROR: Producto agotado (Out of Stock).");
        }

        Optional<CartElement> existingElement = cartElementRepository.findByCartAndProduct(cart, product);

        if (existingElement.isPresent()) {
            CartElement element = existingElement.get();

            if (element.getQuantity() >= product.getStock()) {
                throw new RuntimeException("LIMIT REACHED: No puedes añadir más unidades. Stock máximo alcanzado (" + product.getStock() + ").");
            }

            element.setQuantity(element.getQuantity() + 1);
            cartElementRepository.save(element);
        } else {
            CartElement newElement = new CartElement();
            newElement.setCart(cart);
            newElement.setProduct(product);
            newElement.setQuantity(1);
            cartElementRepository.save(newElement);
        }

        return buildCartDTO(cart);
    }

    @Override
    @Transactional
    @CachePut(value ="carts", key = "#userEmail")
    public CartDTO removeProductFromCart(String userEmail, Long productId) {
        Cart cart = getOrCreateCart(userEmail);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        cartElementRepository.findByCartAndProduct(cart, product)
                .ifPresent(cartElementRepository::delete);

        return buildCartDTO(cart);
    }

    @Override
    @Transactional
    @CacheEvict(value ="carts", key = "#userEmail")
    public void checkout(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Cart cartEntity = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (cartEntity.getItems() == null || cartEntity.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        for (CartElement item : cartEntity.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("ERROR CRÍTICO: Stock insuficiente para el producto: " + product.getName());
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        try {
            CartDTO cartDTO = buildCartDTO(cartEntity);
            emailService.sendOrderConfirmation(userEmail, cartDTO);
        } catch (Exception e) {
            System.err.println("No se pudo enviar el correo de confirmación: " + e.getMessage());
        }

        cartElementRepository.deleteAll(cartEntity.getItems());
        cartEntity.getItems().clear();
        cartRepository.save(cartEntity);
    }
}