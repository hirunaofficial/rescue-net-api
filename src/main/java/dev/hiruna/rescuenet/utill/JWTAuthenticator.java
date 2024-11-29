package dev.hiruna.rescuenet.utill;

import dev.hiruna.rescuenet.entity.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JWTAuthenticator {

    private final String jwtSecret;
    private final long jwtExpirationMs = 86400000; // 1 day in milliseconds

    public JWTAuthenticator() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .filename(".env")
                .load();

        jwtSecret = dotenv.get("SECRET_KEY");

        if (jwtSecret == null || jwtSecret.isEmpty()) {
            throw new IllegalStateException("JWT Secret Key is not configured in the .env file.");
        }
    }

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();  // Generate the JWT string
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public boolean validateJwtToken(String authToken) {
        if (!authToken.startsWith("Bearer ")) {
            return false;
        }

        String jwtToken = authToken.substring(7);

        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(jwtToken);

            return true;
        } catch (JwtException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
        }
        return false;
    }

    public Map<String, Object> getJwtPayload(String authToken) {
        if (!authToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("JWT token must start with 'Bearer '");
        }

        String jwtToken = authToken.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();

            return claims; // Return claims as a Map
        } catch (JwtException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            return null;
        }
    }
}