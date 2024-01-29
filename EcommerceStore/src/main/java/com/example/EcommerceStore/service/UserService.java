package com.example.EcommerceStore.service;


import com.example.EcommerceStore.request.RegisterRequest;
import com.example.EcommerceStore.response.RegisterResponse;

public interface UserService {

  boolean register(RegisterRequest registerRequest);

  void verify(String email,String otp);
}
