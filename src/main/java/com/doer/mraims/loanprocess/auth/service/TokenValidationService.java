package com.doer.mraims.loanprocess.auth.service;

import com.doer.mraims.loanprocess.auth.model.AuthUser;
import com.doer.mraims.loanprocess.core.helper.CommonObjectResponseDTO;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.doer.mraims.loanprocess.core.utils.Constants.*;

@Service
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

    public AuthUser validateAndFetchUser(String authorizationHeader) {
        String token = jwtTokenUtil.extractTokenFromHeader(authorizationHeader);
        CommonObjectResponseDTO<Claims> claims = jwtTokenUtil.validateToken(token);
        String userId = validateAndExtractClaims(authorizationHeader).getData().get("user_id").toString();

        Map<String, Object> tokenUserMap;
        if (ORACLE.equalsIgnoreCase(dbType)) {
            tokenUserMap = tokenUserStatusService.getOracleTokenUserStatus(userId);

        } else if (POSTGRES.equalsIgnoreCase(dbType)) {
            tokenUserMap = tokenUserStatusService.getPostgresTokenUserStatus(userId);
        } else {
            throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
        String revoked = (String) tokenUserMap.get("revoked");
        String status = (String) tokenUserMap.get("status");
        if (revoked != null && status != null && revoked.equalsIgnoreCase("No") && status.equalsIgnoreCase("Signin")) {
            AuthUser authUser = new AuthUser();
            authUser.setUserId(userId);
            authUser.setUserRole((String) claims.getData().get("role")); // Assuming "userName" exists in claims
            authUser.setSchemaName((String) claims.getData().get("sname"));     // Assuming "schemaName" exists in claims
//                authUser.setUserId((String) claims.getData().get("sname")); // Assuming "userName" exists in claims
//                authUser.setUserType((String) claims.getData().get("userType")); // Assuming "userType" exists in claims
//                authUser.setMfiId((String) claims.getData().get("mfiId"));       // Assuming "mfiId" exists in claims
//                authUser.setInstituteOid((String) claims.getData().get("instituteOid")); // Assuming "instituteOid" exists in claims
//                authUser.setOfficeId((String) claims.getData().get("officeId"));         // Assuming "officeId" exists in claims
            return authUser;
        } else {
            throw new IllegalArgumentException("Token is revoked or user is not signed in");
        }
    }
}
