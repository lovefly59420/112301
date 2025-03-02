package com.shopping_cart_project.shopping_cart_project.repository;

import com.shopping_cart_project.shopping_cart_project.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.userId = :userId")
    public List<Order> findOrderByUserId(@Param("userId") Long userId);
}
