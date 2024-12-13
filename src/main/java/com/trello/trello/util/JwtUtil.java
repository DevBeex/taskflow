package com.trello.trello.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.*;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.trello.trello.model.User;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT incluyendo el user_id en las claims.
     *
     * @param user Usuario autenticado.
     * @return Token JWT generado.
     */
    public String generateJwtToken(User user) {
        logger.debug("Generating token for user ID: {}", user.getId());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("user_id", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    /**
     * Obtiene la clave de firma para el token JWT.
     *
     * @return Clave de firma en formato byte array.
     */
    private byte[] getSigningKey() {
        return jwtSecret.getBytes();
    }

    /**
     * Valida el token JWT.
     *
     * @param authToken Token JWT a validar.
     * @return Verdadero si el token es válido, de lo contrario, falso.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Extrae el `user_id` del token JWT.
     *
     * @param token Token JWT del cual extraer el `user_id`.
     * @return `user_id` si está presente, de lo contrario, null.
     */
    public Long getUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("user_id", Long.class);
    }

    /**
     * Extrae el `username` del token JWT.
     *
     * @param token Token JWT del cual extraer el `username`.
     * @return `username` si está presente, de lo contrario, null.
     */
    public String getUsernameFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}