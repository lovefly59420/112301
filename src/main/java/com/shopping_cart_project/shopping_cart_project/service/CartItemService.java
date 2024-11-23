package com.shopping_cart_project.shopping_cart_project.service;

import com.shopping_cart_project.shopping_cart_project.entity.Cart;
import com.shopping_cart_project.shopping_cart_project.entity.CartItem;
import com.shopping_cart_project.shopping_cart_project.entity.Product;
import com.shopping_cart_project.shopping_cart_project.entity.User;
import com.shopping_cart_project.shopping_cart_project.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserService userService;

    public CartItem isCartItemInCart(Cart cart, Product product) {
        return cartItemRepository.isCartItemInCart(cart, product);
    }

    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(Math.max(cartItem.getQuantity(), 1));
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());

        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem item = this.findCartItemById(id);
        User user = userService.findUserById(item.getCart().getUser().getId());
        if(user.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
        }

        return cartItemRepository.save(item);
    }


    public CartItem findCartItemById(Long id) throws Exception {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(id);
        if(optionalCartItem.isPresent()) {
            return optionalCartItem.get();
        }
        throw new Exception("CartItem not found with id : " + id);
    }

    public void removeCartItem(Long userId, Long id) throws Exception {
        CartItem item = findCartItemById(id);
        User user = userService.findUserById(item.getCart().getUser().getId());
        User reqUser = userService.findUserById(userId);
        if(user.getId().equals(reqUser.getId())) {
            cartItemRepository.deleteById(id);
            return;
        }
        throw new Exception("Can't remove another users item");
    }
}
