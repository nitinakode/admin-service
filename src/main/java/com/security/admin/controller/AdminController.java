package com.security.admin.controller;


import com.security.admin.entity.User;
import com.security.admin.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private UserRepository userRepository;
    @GetMapping("/admin1")
    public String getAdmin() {

        System.out.println("answer is admin");
        return "Called by admin";

    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        System.out.println(principal+"  principal");

        String username = principal.getName();  // Username of the authenticated user
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


}