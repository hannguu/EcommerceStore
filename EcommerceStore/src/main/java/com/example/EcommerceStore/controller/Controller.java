package com.example.EcommerceStore.controller;

import com.example.EcommerceStore.entity.Product;
import com.example.EcommerceStore.entity.User;
import com.example.EcommerceStore.repository.ProductRepository;
import com.example.EcommerceStore.repository.UserRepository;
import java.awt.print.PrinterJob;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Controller {

  @Autowired
  public UserRepository userRepository;
  @Autowired
  public ProductRepository productRepository;
  @Autowired
  public PasswordEncoder passwordEncoder;



  @PostMapping("/user/save")
  public ResponseEntity saveUser(@RequestBody User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User result = userRepository.save(user);
    if (result.getUser_id() > 0) {
      return ResponseEntity.ok("User saved");
    }
    return ResponseEntity.status(404).body("User is not saved");
  }

  @GetMapping("/product/all")
  public String getAllProducts(Model model) {
    Product product = productRepository.findById(1).orElse(null);
    model.addAttribute("product", product);
    return "product";
  }

  @GetMapping("/users/all")
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  public ResponseEntity<Object> getAllUsers() {
    return ResponseEntity.ok(userRepository.findAll());
  }


  @GetMapping("/users/single")
  public ResponseEntity<Object> getUserDetails() {
    return ResponseEntity.ok(userRepository
        .findByEmail(Objects.requireNonNull(getLoggedInUserDetails()).getUsername()));
  }

  private UserDetails getLoggedInUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(authentication != null && authentication.getPrincipal() instanceof UserDetails)
    {
      return (UserDetails)  authentication.getPrincipal();
    }
    return null;
  }
}
