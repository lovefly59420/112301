package com.shopping_cart_project.shopping_cart_project.controller;

import com.shopping_cart_project.shopping_cart_project.entity.CartItem;
import com.shopping_cart_project.shopping_cart_project.entity.User;
import com.shopping_cart_project.shopping_cart_project.service.CartItemService;
import com.shopping_cart_project.shopping_cart_project.service.CartService;
import com.shopping_cart_project.shopping_cart_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cartItem")
public class CartItemController {

    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;

    // 修改商品數量。在知名網站中，點擊加號或減號，增加或減少商品數量，就是在執行和這段類似的程式碼
    @PutMapping("/{cartItemId}")
    public ResponseEntity<String> updateCartItem(@PathVariable("cartItemId") Long id,
                                                 @RequestBody CartItem cartItem,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJWT(jwt);
        cartItemService.updateCartItem(user.getId(), id, cartItem);

        return new ResponseEntity<>("Cart item updated successfully", HttpStatus.OK);
    }


    // 刪除商品的功能
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable("cartItemId") Long id,
                                                 @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJWT(jwt);
        cartItemService.removeCartItem(user.getId(), id);
        return new ResponseEntity<>("CartItem deleted successfully", HttpStatus.OK);
    }
}
