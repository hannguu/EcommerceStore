package com.example.EcommerceStore.config;

import com.example.EcommerceStore.entity.User;
import com.example.EcommerceStore.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Configuration
public class OurUserDetailsService implements UserDetailsService {
  
  private final UserRepository userRepository;

  @Autowired
  public OurUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String user_name) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByEmail(user_name);
    return user.map(UserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
  }
}
