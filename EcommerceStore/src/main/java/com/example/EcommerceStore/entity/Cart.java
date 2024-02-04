package com.example.EcommerceStore.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_id")
  private int cartId;

  @Column(name = "user_id")
  private int userId;


  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
  private List<CartItem> cartItemList = new ArrayList<>();

}
