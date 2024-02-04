package com.example.EcommerceStore.repository;

import com.example.EcommerceStore.entity.CartItem;
import com.example.EcommerceStore.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
  List<CartItem> findCartItemsByCartId(int cartId);
  CartItem findCartItemByCartItemId(int cartItemId);

}
