package com.example.EcommerceStore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="product_id")
  private int productId;
  private String product_name;
  private int product_quantity;
  @Column(name="product_price")
  private int productPrice;
  private String product_image;
  private int rating;
  @Column(name="product_brand")
  private String productBrand;
  private String productType;
}
