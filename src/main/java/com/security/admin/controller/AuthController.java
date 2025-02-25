package com.security.admin.controller;

import com.security.admin.dto.AuthRequest;
import com.security.admin.entity.User;
import com.security.admin.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired private AuthService service;

  @Autowired private AuthenticationManager authenticationManager;

  @PostMapping("/register")
  public String addNewUser(@RequestBody User user) {
    return service.saveUser(user);
  }

  @PostMapping("/login")
  public String getToken(@RequestBody AuthRequest authRequest) {

    return service.generateToken(authRequest.getUsername(), "ADMIN");
  }

  @GetMapping("/validate")
  public String validateToken(@RequestParam("token") String token) {
    service.validateToken(token);
    return "Token is valid";
  }

  @GetMapping("/user/{id}")
  @PreAuthorize("hasAuthority('ADMIN')") // Use 'hasAuthority' instead of 'hasRole'
  public User userInfo(@PathVariable("id") Long id) {
    return service.findById(id); // If allowed, return the user info
  }
}
