package com.shopping_cart_project.shopping_cart_project.service;

import com.shopping_cart_project.shopping_cart_project.entity.Cart;
import com.shopping_cart_project.shopping_cart_project.entity.CartItem;
import com.shopping_cart_project.shopping_cart_project.entity.Product;
import com.shopping_cart_project.shopping_cart_project.entity.User;
import com.shopping_cart_project.shopping_cart_project.repository.CartRepository;
import com.shopping_cart_project.shopping_cart_project.repository.ProductRepository;
import com.shopping_cart_project.shopping_cart_project.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartItemService cartItemService;

    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    public void addToCart(Long userId, AddItemRequest req) throws Exception{
        Cart cart = cartRepository.findCartByUserId(userId);
        Product product = productService.getProductById(req.getProductId());
        CartItem isPresent = cartItemService.isCartItemInCart(cart, product);
        if(isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setPrice(req.getQuantity() * product.getPrice());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);

            this.calcCartTotal(userId);
        }
    }

    public Cart calcCartTotal(Long userId) {
        Cart cart = cartRepository.findCartByUserId(userId);
        int totalPrice = 0, totalQuantity = 0;

        for(CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getPrice();
            totalQuantity += cartItem.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalQuantity(totalQuantity);
        return cartRepository.save(cart);
    }

    public Integer clearCart(Long userId) throws Exception {
        Cart cart = cartRepository.findCartByUserId(userId);
        Integer totalPrice = cart.getTotalPrice();

        Iterator<CartItem> iterator = cart.getCartItems().iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            cartItemService.removeCartItem(userId, cartItem.getId());
            iterator.remove();
        }

        cart.setTotalPrice(0);
        cart.setTotalQuantity(0);
        cartRepository.save(cart);

        return totalPrice;
    }
}
