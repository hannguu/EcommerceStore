package com.example.EcommerceStore.repository;

import com.example.EcommerceStore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
  Cart findCartByUserId(int userId);


}
