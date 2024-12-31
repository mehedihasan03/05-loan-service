package com.doer.mraims.loanprocess.auth.service;

import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@Slf4j
public class JwtTokenUtil {
    @Value("${access.token.secret}")
    private String jwtSecretKey;

    // Added by Kamal Parvez on 2024-12-31 to validate token
    public CommonObjectResponseDTO<Claims>  validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (claims.get("user_id") == null || claims.get("user_id").toString().isEmpty()) {
                return new CommonObjectResponseDTO<>(false, 401, "Unauthorized", null);
            }
            return new CommonObjectResponseDTO<>(true, 200, "Token is valid", claims);
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT signature");
        } catch (JwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid JWT token");
        } catch (Exception e) {
            log.error("Unexpected error during JWT validation: {}", e.getMessage());
            throw new IllegalArgumentException("Unexpected error during JWT validation");
        }
    }

    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header must start with 'Bearer '");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }
}