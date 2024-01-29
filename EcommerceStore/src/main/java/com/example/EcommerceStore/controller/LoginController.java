package com.example.EcommerceStore.controller;

import com.example.EcommerceStore.entity.User;
import com.example.EcommerceStore.repository.UserRepository;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/EcommerceStore")
public class LoginController {

  @Autowired
  PasswordEncoder passwordEncoder;
  @Autowired
  UserRepository userRepository;

  @GetMapping("/loginpage")
  public String login() {
    return "login";
  }

  @PostMapping("/login")
  public String login(@RequestParam String username, @RequestParam String password, Model model) {
    // Perform proper authentication logic here (consider using Spring Security)

    // Example using your repository methods (not recommended for production)
    Optional<User> user = userRepository.findByEmail(username);
    if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
      // Authentication successful, redirect to the product page
      return "redirect:/EcommerceStore/product";
    } else {
      // Authentication failed, set error message and return to login page
      model.addAttribute("errorlogin", "Invalid username or password");
      return "login";
    }
  }


  @GetMapping("/signingoogle")
  public String currentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
    Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();

    // Convert attributes to User object
    User user = toUser(attributes);
    if (!userRepository.existsUserByUserEmail(user.getUserEmail())) {
      userRepository.save(user);
    }
    return "redirect:/EcommerceStore/product";
  }

  public User toUser(Map<String, Object> map) {
    if (map == null) {
      return null;
    }
    User user = new User();
    user.setUserEmail((String) map.get("email"));
    user.setUser_name((String) map.get("name"));
    user.setRoles("USER");
    user.setPassword("");
    user.setUser_phoneNumber(""); 
    user.setBirthday(Timestamp.valueOf("1990-01-01 00:00:00"));

    return user;
  }

  @GetMapping("/signinfacebook")

  public String currentFacebookUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
    Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();

    // Convert attributes to User object for Facebook
    User user = toFacebookUser(attributes);

    if (!userRepository.existsUserByUserEmail(user.getUserEmail())) {
      userRepository.save(user);
    }
    return "redirect:/EcommerceStore/product";
  }




  public User toFacebookUser(Map<String, Object> map) {
    if (map == null) {
      return null;
    }

    User user = new User();
    user.setUserEmail((String) map.get("email"));
    user.setUser_name((String) map.get("name"));
    user.setRoles("USER");
    user.setPassword(""); // You may want to handle this differently for Facebook
    user.setUser_phoneNumber("0123123123"); // You may want to handle this differently for Facebook
    user.setBirthday(Timestamp.valueOf("1990-01-01 00:00:00"));

    return user;
  }


}