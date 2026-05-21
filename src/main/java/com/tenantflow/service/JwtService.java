package com.tenantflow.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    /*
     * Secret Key
     */
    @Value("${jwt.secret:mySuperSecretKeyForJwtGeneration}")
    private String secretKey;

    /*
     * Generate JWT Token
     */
    public String generateToken(String email, String role, String tenantId) {

        java.security.Key key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .claim("role", role)
                .claim("tenantId", tenantId)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    /*
     * Extract Email
     */
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    /*
     * Extract Role
     */
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    /*
     * Extract TenantId
     */
    public String extractTenantId(String token) {
        return extractClaims(token).get("tenantId", String.class);
    }

    /*
     * Validate Token
     */
    public boolean isTokenValid(String token) {
        return !extractClaims(token).getExpiration().before(new Date());
    }

    /*
     * Extract Claims
     */
    private Claims extractClaims(String token) {
        java.security.Key key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(secretKey.getBytes());
        
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
