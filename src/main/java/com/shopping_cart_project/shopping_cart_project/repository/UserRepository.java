package com.shopping_cart_project.shopping_cart_project.repository;

import com.shopping_cart_project.shopping_cart_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
