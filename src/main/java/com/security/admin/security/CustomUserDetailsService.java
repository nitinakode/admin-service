package com.security.admin.security;

import com.security.admin.entity.User;
import com.security.admin.repo.UserRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired private UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOptional = repository.findByUsername(username);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      return new org.springframework.security.core.userdetails.User(
          user.getUsername(), user.getPassword(), getAuthorities(user));
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }

  private Collection<? extends GrantedAuthority> getAuthorities(User user) {
    return Collections.singletonList(
        new SimpleGrantedAuthority(
            "ROLE_"
                + user.getRoles().stream()
                    .findFirst()
                    .get()
                    .getName())); // Map the role to GrantedAuthority
  }
}
