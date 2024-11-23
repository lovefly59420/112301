package com.shopping_cart_project.shopping_cart_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @OneToOne代表的是一對一，這個entity會對應到另一個entity。
    // @JoinColumn表示我們會在Cart的資料表中，多增加一列用來儲存用戶id的數值，這一列的名字是user_id，不能是null。
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // @OneToMany代表一對多的關係，因為一個購物車裡面可以有很多物品。
    // mappedBy = "cart” --> 表示cart這邊不去處理與CartItem間的關聯，而是交給CartItem處理關聯，因此，cart資料表不會出現cartItem_id的欄位
    // CascadeType.ALL --> 表示對Cart entity的任何操作都會傳播到CartItem entity。目的是清空購物車時，購物車中的物品也一併刪除。
    // orphanRemoval = true --> 如果我們在cart刪除購物車物品，那麼cartItem的內容也會被刪除。
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();
    private Integer totalPrice;
    private Integer totalQuantity;



    // 為了解決 java.lang.StackOverflowError: null --> 雙向關聯的循環調用導致的無限遞歸，Hibernate 和 JPA 中，這通常與實體類之間的雙向關聯有關
    // 也可在entity 使用 @EqualsAndHashCode 注解
    // @EqualsAndHashCode(onlyExplicitlyIncluded = true) // 僅基於標註的屬性生成方法，需在id加上註解@EqualsAndHashCode.Include
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id); // 基於 id 判斷相等
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // 基於 id 計算哈希
    }
}
