package com.security.admin.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.security.Key;
import java.util.Collections;
import java.util.List;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";  // Your secret key

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null) {
            try {
                if (validateToken(token,response)) {
                    String username = getUsernameFromToken(token);
                    List<GrantedAuthority> authorities = getAuthoritiesFromToken(token);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else
                {
                    return;
                }

            } catch (Exception e) {
                handleException(response, "Invalid token", HttpServletResponse.SC_FOUND);
                return;  // Stop further processing for invalid token
            }
        }

        filterChain.doFilter(request, response);  // Proceed with the filter chain
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Remove "Bearer " prefix
        }
        return null;
    }

    private boolean validateToken(String token, HttpServletResponse response) throws IOException {
        try {
            // Attempt to parse and validate the token with the signing key
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;  // Token is valid if no exception is thrown
        } catch (ExpiredJwtException e) {
            // Token is expired
            handleException(response, "Token has expired", HttpServletResponse.SC_UNAUTHORIZED);
            return false;  // Token has expired
        } catch (MalformedJwtException e) {
            // Malformed token
            handleException(response, "Malformed JWT token", HttpServletResponse.SC_BAD_REQUEST);
            return false;  // Malformed JWT token
        } catch (UnsupportedJwtException e) {
            // Unsupported token type
            handleException(response, "Unsupported JWT token", HttpServletResponse.SC_BAD_REQUEST);
            return false;  // Unsupported JWT token
        } catch (IllegalArgumentException e) {
            // Token is empty or invalid
            handleException(response, "JWT claims string is empty", HttpServletResponse.SC_BAD_REQUEST);
            return false;  // Invalid JWT claims
        } catch (JwtException e) {
            // Any other JWT-related error
            handleException(response, "Invalid JWT token", HttpServletResponse.SC_UNAUTHORIZED);
            return false;  // Token is invalid due to some other reason
        }
    }


    private String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();  // Extract username (subject) from the token
    }

    private List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        String role = claims.get("role", String.class);
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));  // Add role as authority
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private void handleException(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);  // Set the appropriate HTTP status (401 for Unauthorized)
        response.setContentType("application/json");  // Set response content type to JSON
        response.getWriter().write("{\"message\": \"" + message + "\"}");  // Write the error message to the response body
        response.getWriter().flush();  // Ensure the message is sent to the client
    }
}
