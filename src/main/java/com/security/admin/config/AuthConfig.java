package com.security.admin.config;

import com.security.admin.exception.CustomAccessDeniedHandler;
import com.security.admin.security.CustomUserDetailsService;
import com.security.admin.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AuthConfig {

  @Autowired private CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .requestMatchers("/auth/register", "/auth/login", "/auth/validate")
        .permitAll() // Allow access to these endpoints
        .requestMatchers("/admin/**")
        .hasRole("ADMIN") // Restrict /admin/** to ADMIN role
        .anyRequest()
        .authenticated() // All other endpoints require authentication
        .and()
        .exceptionHandling()
        .accessDeniedHandler(
            customAccessDeniedHandler) // Use the custom access denied handler for authorization
        // issues
        .and()
        .addFilterBefore(
            new JwtTokenFilter(),
            UsernamePasswordAuthenticationFilter
                .class); // Add JWT token filter before default authentication filter

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
