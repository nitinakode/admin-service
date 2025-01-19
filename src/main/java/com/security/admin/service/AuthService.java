package com.security.admin.service;



import com.security.admin.entity.User;
import com.security.admin.repo.UserRepository;
import com.security.admin.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(User credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return "user added to the system";
    }

    public String generateToken(String username,String role2) {
       String role= repository.findByUsername(username).get().getRoles().stream().findFirst().get().getName();
        return jwtService.generateToken(username,role);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
    public User findById(Long id) {
    return     repository.findById(id).orElseThrow();
    }
}