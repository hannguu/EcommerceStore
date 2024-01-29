package com.example.EcommerceStore.repository;

import com.example.EcommerceStore.entity.Product;
import com.example.EcommerceStore.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
  List<Product> findProductByProductType(String productType);
  List<Product> findProductsByProductBrand(String productBrand);
}
