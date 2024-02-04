package com.example.EcommerceStore.controller;

import com.example.EcommerceStore.entity.Cart;
import com.example.EcommerceStore.entity.CartItem;
import com.example.EcommerceStore.entity.Product;
import com.example.EcommerceStore.entity.User;
import com.example.EcommerceStore.repository.CartItemRepository;
import com.example.EcommerceStore.repository.CartRepository;
import com.example.EcommerceStore.repository.ProductRepository;
import com.example.EcommerceStore.repository.UserRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/EcommerceStore")
public class CartController {

  @Autowired
  CartRepository cartRepository;
  @Autowired
  CartItemRepository cartItemRepository;
  @Autowired
  ProductRepository productRepository;
  @Autowired
  UserRepository userRepository;


  @GetMapping("/cart/{user_id}")
  public String viewCart(@PathVariable("user_id") int user_id, Model model) {
    Cart cart = cartRepository.findCartByUserId(user_id);

    if (cart == null) {
      model.addAttribute("error", "Your cart is empty");
    } else {
      List<CartItem> cartItemList = cartItemRepository.findCartItemsByCartId(cart.getCartId());
      model.addAttribute("productRepository", productRepository);
      model.addAttribute("cartItemList", cartItemList);
      model.addAttribute("user_id", user_id);
      int total = 0;
      for (CartItem c : cartItemList) {
        total += c.getQuantity() * productRepository.getProductByProductId(c.getProductId())
            .getProductPrice();
      }
      model.addAttribute("total", total);
    }

    return "cart";
  }

  @PostMapping("/cart/add")
  public String addToCart(@RequestParam("product_id") int product_id,
      @RequestParam("user_id") int user_id, Model model) {
    try {
      Optional<Product> productOptional = productRepository.findById(product_id);

      if (productOptional.isPresent()) {
        Product product = productOptional.get();

        // Retrieve or create a user based on the user_id
        User user = userRepository.findById(user_id).orElse(null);

        if (user != null) {
          Cart cart = getCurrentUserCart(user.getUser_id());
          Optional<CartItem> existingItem = cart.getCartItemList().stream()
              .filter(item -> item.getProductId() == (product.getProductId()))
              .findFirst();

          if (existingItem.isPresent()) {
            // If the product is already in the cart, increase the quantity
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
          } else {
            // If the product is not in the cart, create a new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setProductId(product.getProductId());
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cartItem.setCartId(cart.getCartId());
            cart.getCartItemList().add(cartItem);
            cartItemRepository.save(cartItem);

          }
          List<CartItem> cartItemList = cartItemRepository.findCartItemsByCartId(cart.getCartId());
          model.addAttribute("cartItemList", cartItemList);
          int total = 0;
          for (CartItem c : cartItemList) {
            total += c.getQuantity() * productRepository.getProductByProductId(c.getProductId())
                .getProductPrice();
          }
          model.addAttribute("total", total);
          model.addAttribute("productRepository", productRepository);
          model.addAttribute("user_id", user_id);
          // Set the user in the cart
          model.addAttribute("userRepository", userRepository);
          cart.setUserId(user_id);
          cartRepository.save(cart);

          return "cart";
        } else {
          // Log or handle the case where the user does not exist
          // Return an appropriate error response or redirect
          return "error";
        }
      } else {
        // Product not found, handle accordingly
        return "error";
      }
    } catch (Exception ex) {
      model.addAttribute("error", ex);
      return "error";
    }
  }

  // get cart of current user
  private Cart getCurrentUserCart(int user_id) {
    Cart c = cartRepository.findCartByUserId(user_id);
    if (c != null) {
      return cartRepository.findCartByUserId(user_id);
    } else {
      return new Cart();
    }

  }

  // update quantity of product
  @PostMapping("/cart/updateQuantity")
  public String updateQuantity(@RequestParam("cartItemId") int cartItemId,

      @RequestParam("action") String action,
      @RequestParam("user_id") int user_id,
      Model model) {
    try {
      CartItem cartItem = cartItemRepository.findById(cartItemId)
          .orElseThrow(() -> new IllegalArgumentException("Invalid cart item Id: " + cartItemId));
      Cart cart = cartRepository.findCartByUserId(user_id);
      if ("increase".equalsIgnoreCase(action)) {
        cartItem.setQuantity(cartItem.getQuantity() + 1);

      } else if ("decrease".equalsIgnoreCase(action)) {
        if (cartItem.getQuantity() > 1) {
          cartItem.setQuantity(cartItem.getQuantity() - 1);
          cartItemRepository.save(cartItem);
        } else {
          removeItem(cartItemId, user_id, model);
          return "cart";
        }
      }
      List<CartItem> cartItemList = cartItemRepository.findCartItemsByCartId(cart.getCartId());
      model.addAttribute("cartItemList", cartItemList);
      int total = 0;
      for (CartItem c : cartItemList) {
        total += c.getQuantity() * productRepository.getProductByProductId(c.getProductId())
            .getProductPrice();
      }
      model.addAttribute("total", total);
      model.addAttribute("productRepository", productRepository);
      model.addAttribute("user_id", user_id);
      cartItemRepository.save(cartItem);

      return "cart";
    } catch (Exception ex) {
      model.addAttribute("error", ex.getMessage());
      return "error";
    }
  }

  @PostMapping("/cart/removeItem")
  public String removeItem(@RequestParam("cartItemId") int cartItemId,
      @RequestParam("user_id") int user_id, Model model) {
    try {
      CartItem cartItem = cartItemRepository.findById(cartItemId)
          .orElseThrow(() -> new IllegalArgumentException("Invalid cart item Id: " + cartItemId));
      Cart cart = cartRepository.findCartByUserId(user_id);
      cartItemRepository.delete(cartItem);
      // You may want to update the cart or related data here if needed
      List<CartItem> cartItemList = cartItemRepository.findCartItemsByCartId(cart.getCartId());
      model.addAttribute("cartItemList", cartItemList);
      int total = 0;
      for (CartItem c : cartItemList) {
        total += c.getQuantity() * productRepository.getProductByProductId(c.getProductId())
            .getProductPrice();
      }
      model.addAttribute("total", total);
      model.addAttribute("productRepository", productRepository);
      model.addAttribute("user_id", user_id);
      return "cart";
    } catch (Exception ex) {
      model.addAttribute("error", ex.getMessage());
      return "error";
    }
  }


}
