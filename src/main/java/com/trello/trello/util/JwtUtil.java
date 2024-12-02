package com.trello.trello.util;

import com.trello.trello.constants.JwtMessages;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final String jwtSecret = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
    private final int jwtExpirationMs = 3600000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateJwtToken(String username) {
        System.out.println("Generating token for: " + username);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println(JwtMessages.EXPIRED + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println(JwtMessages.UNSUPPORTED + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println(JwtMessages.INVALID + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(JwtMessages.ILLEGAL_ARGUMENT + e.getMessage());
        }
        return false;
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
