package com.doer.mraims.loanprocess.auth.service;

import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.doer.mraims.loanprocess.core.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class TokenValidationService {
    @Value("${database.platform.active}")
    private String dbType;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TokenUserStatusService tokenUserStatusService;

    public CommonObjectResponseDTO<Claims> validateAndExtractClaims(String authorizationHeader) {
        String token = jwtTokenUtil.extractTokenFromHeader(authorizationHeader);
        return jwtTokenUtil.validateToken(token);
    }

    public JSONObject validateAndFetchUser(String authorizationHeader) {
        // Extract and validate the JWT token
        String token = jwtTokenUtil.extractTokenFromHeader(authorizationHeader);
        CommonObjectResponseDTO<Claims> decoded = jwtTokenUtil.validateToken(token);
        String userId = decoded.getData().get("user_id").toString();

        if (ORACLE.equalsIgnoreCase(dbType)) {
            return tokenUserStatusService.getOracleTokenUserStatus(userId);
        } else if (POSTGRES.equalsIgnoreCase(dbType)) {
            return tokenUserStatusService.getPostgresTokenUserStatus(userId);
        } else {
            throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }
}
