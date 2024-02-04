package com.example.EcommerceStore.controller;

import com.example.EcommerceStore.config.UserInfoDetails;
import com.example.EcommerceStore.entity.Product;
import com.example.EcommerceStore.entity.User;
import com.example.EcommerceStore.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner.Mode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.EcommerceStore.repository.ProductRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/EcommerceStore")
@Controller
public class ProductController {

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private UserRepository userRepository;

  @GetMapping("/product")
  public String getProduct(Authentication authentication, Model model) {
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof UserDetails userDetails) {
        // Standard UserDetails case
        String email = userDetails.getUsername();
        model.addAttribute("user_email", email);
        User user = userRepository.findByUserEmail(email);
        model.addAttribute("userRepository", userRepository);
        int user_id = user.getUser_id();
        model.addAttribute("user_id", user_id);
      } else if (principal instanceof OAuth2User oAuth2User) {
        // get user_email when sign in with google or facebook
        Map<String, Object> attributes = oAuth2User.getAttributes();
        model.addAttribute("user_email",
            attributes.get("email"));

        model.addAttribute("userRepository", userRepository);


      } else {
        return "error";
      }
    }
    List<Product> productList = productRepository.findAll();
    model.addAttribute("productList", productList);

    List<Product> listPhone = productRepository.findProductByProductType("Phone");
    model.addAttribute("listPhone", listPhone);
    List<Product> listLaptop = productRepository.findProductByProductType("Laptop");
    model.addAttribute("listLaptop", listLaptop);
    List<Product> listEarPhone = productRepository.findProductByProductType("Ear Phone");
    model.addAttribute("listEarPhone", listEarPhone);
    return "homepage";
  }

  @GetMapping("/productDetails/{productId}")

  public String getProductDetails(@PathVariable("productId") Integer productId,
      Authentication authentication, Model model) {
    if (authentication != null && authentication.isAuthenticated()) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof UserDetails userDetails) {
        // Standard UserDetails case
        String email = userDetails.getUsername();
        model.addAttribute("user_email", email);
        User user = userRepository.findByUserEmail(email);
        model.addAttribute("userRepository", userRepository);
        int user_id = user.getUser_id();
        model.addAttribute("user_id", user_id);
      } else if (principal instanceof OAuth2User oAuth2User) {
        // get user_email when sign in with google or facebook
        Map<String, Object> attributes = oAuth2User.getAttributes();
        model.addAttribute("user_email",
            attributes.get("email"));
        model.addAttribute("userRepository", userRepository);


      } else {
        return "error";
      }

    }
    Optional<Product> optionalProduct = productRepository.findById(productId);
    if (optionalProduct.isPresent()) {
      model.addAttribute("product", optionalProduct.get());
      return "productDetails";
    }

    return "error";
  }

  @GetMapping("/productFilter/{product_type}")
  public String productFilter(@PathVariable("product_type") String product_type, Model model) {
    List<Product> listProduct = productRepository.findProductByProductType(product_type);
    model.addAttribute("listProduct", listProduct);
    model.addAttribute("productType", product_type);
    return "productFilter";
  }

  @GetMapping("/productBrandFilter/{product_brand}")
  public String findByProductBrand(@PathVariable("product_brand") String product_brand,
      Model model) {
    List<Product> listProduct = productRepository.findProductsByProductBrand(product_brand);
    model.addAttribute("listProduct", listProduct);
    model.addAttribute("productType", "Laptop");
    return "productFilter";
  }

  @GetMapping("/productFilter")
  public String findProductByPrice(@RequestParam("start_price") int start_price,
      @RequestParam("end_price") int end_price, @RequestParam("productType") String productType,
      Model model) {
    List<Product> listProduct = productRepository.findProductsByProductPriceBetweenAndProductType(
        start_price,
        end_price, productType);
    model.addAttribute("listProduct", listProduct);
    model.addAttribute("productType", productType);
    return "productFilter";
  }
}
