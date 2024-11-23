package com.shopping_cart_project.shopping_cart_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;
    private Integer amount;
    private String status;

    // @Column(length = 1024)設定這個欄位的上限是1024字元。
    @Column(length = 1024)
    private String url;
    private Long userId;
}
