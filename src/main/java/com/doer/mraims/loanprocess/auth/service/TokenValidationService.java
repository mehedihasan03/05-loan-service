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
    @Value("${database.platform.active}")
    private String dbType;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthUserRepository authUserRepository;

    public CommonObjectResponseDTO<Claims> validateAndExtractClaims(String authorizationHeader) {
        String token = jwtTokenUtil.extractTokenFromHeader(authorizationHeader);
        return jwtTokenUtil.validateToken(token);
    }

    public AuthUser validateAndFetchUser(String authorizationHeader) {
        CommonObjectResponseDTO<Claims> claims = validateAndExtractClaims(authorizationHeader);
        String userId = claims.getData().get("user_id").toString();

        Map<String, Object> tokenUserMap;
        if (ORACLE.equalsIgnoreCase(dbType)) {
            tokenUserMap = authUserRepository.getOracleTokenUserStatus(userId);

        } else if (POSTGRES.equalsIgnoreCase(dbType)) {
            tokenUserMap = authUserRepository.getPostgresTokenUserStatus(userId);
        } else {
            throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
        String revoked = (String) tokenUserMap.get("revoked");
        String status = (String) tokenUserMap.get("status");
        if (revoked != null && status != null && revoked.equalsIgnoreCase("No") && status.equalsIgnoreCase("Signin")) {
            AuthUser authUser = new AuthUser();
            authUser.setUserId(userId);
            authUser.setUserRole((String) claims.getData().get("role"));
            authUser.setSchemaName((String) claims.getData().get("sname"));
            authUser.setMfiId((String) claims.getData().get("sid"));
            return authUser;
        } else {
            throw new IllegalArgumentException("Token is revoked or user is not signed in");
        }
    }
}
