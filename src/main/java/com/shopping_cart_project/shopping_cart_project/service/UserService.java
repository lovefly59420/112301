package com.shopping_cart_project.shopping_cart_project.service;

import com.shopping_cart_project.shopping_cart_project.config.JWTProvider;
import com.shopping_cart_project.shopping_cart_project.entity.CartItem;
import com.shopping_cart_project.shopping_cart_project.entity.User;
import com.shopping_cart_project.shopping_cart_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTProvider jwtProvider;


    public void createUser(User user) throws Exception {
        User isEmailExisting = userRepository.findByEmail(user.getEmail());

        if(isEmailExisting != null){
            throw new Exception("Error: Email is already registered.");
        }

        User createUser = new User();
        createUser.setEmail(user.getEmail());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(createUser);
    }


    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findUserByJWT(String jwt) throws Exception{
        String email = jwtProvider.getEmailFromJWT(jwt);
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new Exception("Error: Invalid JWT");
        }
        return user;
    }

    public User findUserById(Long id) throws Exception{
        Optional<User> opt = userRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new Exception("Error: User not found with id: " + id);
    }
}
