package com.example.acl.domains.users.models.mappers;

import com.example.acl.domains.users.models.dtos.TokenResponse;
import com.example.acl.domains.users.models.entities.AcValidationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenMapper {
    @Value("${token.validity}")
    private String tokenValidity;

    public TokenResponse map(AcValidationToken token) {
        TokenResponse response = new TokenResponse();
        response.setUsername(token.getUsername());
        response.setTokenValidityMillis(Integer.parseInt(tokenValidity));
        response.setTokenValidUntill(token.getTokenValidUntil());
        return response;
    }

}
