package com.security.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class AuthResponse {
        private String token;

        public AuthResponse(String token) {
            this.token = token;
        }

        // Getters and setters
    }