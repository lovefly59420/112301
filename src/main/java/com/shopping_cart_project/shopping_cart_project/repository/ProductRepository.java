package com.shopping_cart_project.shopping_cart_project.repository;

import com.shopping_cart_project.shopping_cart_project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
            "WHERE (p.category = :category OR :category LIKE '') " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "ORDER BY " +
            "CASE :sort WHEN 'price_low' THEN p.price END ASC, " +
            "CASE :sort WHEN 'price_high' THEN p.price END DESC")
    public List<Product> findProductsByFilter(@Param("category") String category,
                                              @Param("minPrice") Integer minPrice,
                                              @Param("maxPrice") Integer maxPrice,
                                              @Param("sort") String sort);
}
