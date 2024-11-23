package com.shopping_cart_project.shopping_cart_project.controller;

import com.shopping_cart_project.shopping_cart_project.config.JWTProvider;
import com.shopping_cart_project.shopping_cart_project.entity.Cart;
import com.shopping_cart_project.shopping_cart_project.entity.User;
import com.shopping_cart_project.shopping_cart_project.response.AuthResponse;
import com.shopping_cart_project.shopping_cart_project.service.CartService;
import com.shopping_cart_project.shopping_cart_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CartService cartService;


    @GetMapping("/test/")
    public String aaa(){
        return "123456";
    }


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        userService.createUser(user);

        String token = jwtProvider.generateToken(user.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Signup Success!");
        Cart cart = cartService.createCart(userService.findUserByEmail(user.getEmail()));

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody User user) throws Exception {
        User foundUser = userService.findUserByEmail(user.getEmail());

        boolean t1 = passwordEncoder.matches(user.getPassword(), foundUser.getPassword());
        if(foundUser == null || !passwordEncoder.matches(user.getPassword(), foundUser.getPassword())){
            throw new Exception("Invalid email or password");
        }

        String token = jwtProvider.generateToken(foundUser.getEmail());
        AuthResponse authResponse = new AuthResponse(token, "Login Success !");
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
