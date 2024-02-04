package com.example.EcommerceStore.entity;

import com.example.EcommerceStore.repository.ProductRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "[cart_item]")
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_item_id")
  private int cartItemId;
  @Column(name="product_id")
  private int productId;
  private int quantity;
  @Column(name="cart_id")
  private int cartId;
  @ManyToOne
  @JoinColumn(name = "cart_id", insertable = false, updatable = false)
  private Cart cart;

}
