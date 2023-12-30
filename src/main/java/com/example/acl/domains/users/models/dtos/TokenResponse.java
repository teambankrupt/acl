package com.example.acl.domains.users.models.dtos;

import com.example.acl.domains.users.models.enums.AuthMethods;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;

public class TokenResponse {
    @ApiModelProperty(readOnly = true)
    @JsonProperty("username")
    private String username;

    @ApiModelProperty(readOnly = true)
    @JsonProperty("token_valid_until")
    private Instant tokenValidUntill;

    @ApiModelProperty(readOnly = true)
    @JsonProperty("token_validity_millis")
    private long tokenValidityMillis;

    @JsonProperty("reg_method")
    private AuthMethods regMethod;

    public AuthMethods getRegMethod() {
        return regMethod;
    }

    public void setRegMethod(AuthMethods regMethod) {
        this.regMethod = regMethod;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getTokenValidUntill() {
        return tokenValidUntill;
    }

    public void setTokenValidUntill(Instant tokenValidUntill) {
        this.tokenValidUntill = tokenValidUntill;
    }

    public long getTokenValidityMillis() {
        return tokenValidityMillis;
    }

    public void setTokenValidityMillis(long tokenValidityMillis) {
        this.tokenValidityMillis = tokenValidityMillis;
    }
}
