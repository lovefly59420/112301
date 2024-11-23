package com.shopping_cart_project.shopping_cart_project.controller;

import com.shopping_cart_project.shopping_cart_project.entity.Order;
import com.shopping_cart_project.shopping_cart_project.entity.User;
import com.shopping_cart_project.shopping_cart_project.service.CartService;
import com.shopping_cart_project.shopping_cart_project.service.OrderService;
import com.shopping_cart_project.shopping_cart_project.service.UserService;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;


    @GetMapping("/create_session")
    public ResponseEntity<Order> createCheckoutSession(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJWT(jwt);
        Long userId = user.getId();
        Integer totalPrice = cartService.clearCart(userId);
        Session session = orderService.createCheckoutSession(totalPrice);
        Order order = orderService.createOrder(session.getId(), totalPrice, session.getPaymentStatus(), session.getUrl(), userId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // 找尋用戶的全部訂單
    @GetMapping("/find_order")
    public ResponseEntity<List<Order>> findOrderByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJWT(jwt);
        return ResponseEntity.ok(orderService.findOrderByUserId(user.getId()));
    }
}


