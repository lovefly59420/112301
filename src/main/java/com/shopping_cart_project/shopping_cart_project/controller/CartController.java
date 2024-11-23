package com.shopping_cart_project.shopping_cart_project.controller;

import com.shopping_cart_project.shopping_cart_project.entity.Cart;
import com.shopping_cart_project.shopping_cart_project.entity.User;
import com.shopping_cart_project.shopping_cart_project.request.AddItemRequest;
import com.shopping_cart_project.shopping_cart_project.service.CartService;
import com.shopping_cart_project.shopping_cart_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
public class CartController {
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    // 第一個是取得購物車的內容。
    @GetMapping("/")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJWT(jwt);
        Cart cart = cartService.calcCartTotal(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    // 第二個是點擊加入購物車時，將商品加入購物車。
    @PutMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestBody AddItemRequest req, @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJWT(jwt);
        cartService.addToCart(user.getId(), req);
        return new ResponseEntity<>("Item added to cart",HttpStatus.OK);
    }
}
