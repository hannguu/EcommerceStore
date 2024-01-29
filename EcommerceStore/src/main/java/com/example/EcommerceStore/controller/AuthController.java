package com.example.EcommerceStore.controller;

import com.example.EcommerceStore.request.RegisterRequest;
import com.example.EcommerceStore.response.RegisterResponse;
import com.example.EcommerceStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/EcommerceStore")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;

  @GetMapping("/register_form")
  public String register_form()
  {
    return "register_form";
  }
  @PostMapping("/register")
  public String register( RegisterRequest registerRequest, Model model) {
    boolean registerResponse = userService.register(registerRequest);
  if(!registerResponse)
  {
    model.addAttribute("error","Email is already used!");
    return "register_form";
  } else
  {
    return "otp_verify";
  }

  }

  @PostMapping("/verify")
  public String verifyUser(@RequestParam String email, @RequestParam String otp, Model model) {
    try {
      userService.verify(email, otp);
      // You can add attributes to the model if needed
      return "product"; // Thymeleaf template name
    } catch (RuntimeException e) {
      model.addAttribute("error", e.getMessage());
      return "error"; // Thymeleaf template name
    }
  }
}
