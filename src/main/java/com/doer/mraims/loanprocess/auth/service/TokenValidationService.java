package com.doer.mraims.loanprocess.auth.service;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.auth.repository.AuthUserRepository;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.doer.mraims.loanprocess.core.utils.Constants.ORACLE;
import static com.doer.mraims.loanprocess.core.utils.Constants.POSTGRES;

@Service
public class TokenValidationService {
    @Value("${spring.profiles.active}")
    private String dbType;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthUserRepository authUserRepository;

    public AuthUser validateTokenAndExtractCredentialsByApiUrl(String authorizationHeader) {
        String token = jwtTokenUtil.extractTokenFromHeader(authorizationHeader);
        JwtTokenUtil.TokenValidationResponse response = jwtTokenUtil.validateJwtTokenByApiUrl(token);
        if (!response.isValid()) {
            throw new IllegalArgumentException("Token is invalid");
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> credentials = (Map<String, Object>) response.getCredentials();
        return AuthUser.builder()
                .userId((String) credentials.get("user_id"))
                .userRole((String) credentials.get("role"))
                .schemaName((String) credentials.get("sname"))
                .mfiId((String) credentials.get("sid"))
                .build();
    }


    public AuthUser validateAndExtractClaimsBySelf(String authorizationHeader) {
        String token = jwtTokenUtil.extractTokenFromHeader(authorizationHeader);
        CommonObjectResponseDTO<Claims> claimsResponse = jwtTokenUtil.validateJwtTokenSelf(token);
        String userId = claimsResponse.getData().get("user_id").toString();
        Map<String, Object> tokenUserMap = switch (dbType.toUpperCase()) {
            case ORACLE -> authUserRepository.getOracleTokenUserStatus(userId);
            case POSTGRES -> authUserRepository.getPostgresTokenUserStatus(userId);
            default -> throw new IllegalArgumentException("Unsupported database type: " + dbType);
        };
        String revoked = (String) tokenUserMap.get("revoked");
        String status = (String) tokenUserMap.get("status");
        if (!"No".equalsIgnoreCase(revoked) || !"Signin".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("Token is revoked or user is not signed in");
        }
        return AuthUser.builder()
                .userId(userId)
                .userRole((String) claimsResponse.getData().get("role"))
                .schemaName((String) claimsResponse.getData().get("sname"))
                .mfiId((String) claimsResponse.getData().get("sid"))
                .build();
    }

}
