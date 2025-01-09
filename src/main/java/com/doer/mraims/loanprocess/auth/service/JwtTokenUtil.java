package com.doer.mraims.loanprocess.auth.service;

import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class JwtTokenUtil {

    @Value("${access.token.secret}")
    private String jwtSecretKey;


    @Value("${validate.token.url}")
    private String validateTokenUrl;

    private final RestTemplate restTemplate;

    public JwtTokenUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header. It must start with 'Bearer '.");
        }
        return authorizationHeader.replace("Bearer ", "").trim();
    }


    public CommonObjectResponseDTO<Claims> validateJwtTokenSelf(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String userId = claims.get("user_id", String.class);
            if (userId == null || userId.isEmpty()) {
                log.warn("Token validation failed: Missing or empty 'user_id'");
                return new CommonObjectResponseDTO<>(false, 401, "Unauthorized", null);
            }
            return new CommonObjectResponseDTO<>(true, 200, "Token is valid", claims);
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
            return new CommonObjectResponseDTO<>(false, 401, "Invalid JWT signature", null);
        } catch (JwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return new CommonObjectResponseDTO<>(false, 401, "Invalid JWT token", null);
        } catch (Exception e) {
            log.error("Unexpected error during JWT validation: {}", e.getMessage());
            return new CommonObjectResponseDTO<>(false, 500, "Unexpected error during JWT validation", null);
        }
    }


    public TokenValidationResponse validateJwtTokenByApiUrl(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    validateTokenUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                    }
            );
            Map<String, Object> responseData = responseEntity.getBody();

            log.info("Token validation response: {}", responseData);
            if (responseData != null && (int) responseData.getOrDefault("code", 0) == 200) {
                return new TokenValidationResponse(true, responseData.get("data"));
            }
            log.warn("Token validation failed. Response: {}", responseData);
            return new TokenValidationResponse(false, null);
        } catch (HttpClientErrorException e) {
            log.error("HTTP error during token validation: {}", e.getMessage(), e);
            return new TokenValidationResponse(false, null);
        } catch (Exception e) {
            log.error("Unexpected error during token validation: {}", e.getMessage(), e);
            return new TokenValidationResponse(false, null);
        }
    }


    // TokenValidationResponse class to hold the result of token validation
    public record TokenValidationResponse(boolean isValid, @Getter Object credentials) {
    }

}
