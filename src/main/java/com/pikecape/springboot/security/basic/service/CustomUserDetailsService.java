package com.pikecape.springboot.security.basic.service;

import com.pikecape.springboot.security.basic.model.UserEntity;
import com.pikecape.springboot.security.basic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByUsername(username);
    CustomUserDetails customUserDetails;

    if (userEntity != null) {
      customUserDetails = new CustomUserDetails();
      customUserDetails.setUserEntity(userEntity);
    } else {
      throw new UsernameNotFoundException("User " + username + " not found");
    }

    return customUserDetails;
  }
}
