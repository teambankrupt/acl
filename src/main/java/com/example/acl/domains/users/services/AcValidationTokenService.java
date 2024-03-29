package com.example.acl.domains.users.services;


import com.example.acl.domains.users.models.entities.AcValidationToken;
import com.example.auth.entities.User;
import com.example.common.exceptions.forbidden.ForbiddenException;

import java.util.Optional;

public interface AcValidationTokenService {
    AcValidationToken save(AcValidationToken acValidationToken);
    AcValidationToken findOne(Long id);
    AcValidationToken findByToken(String token) throws ForbiddenException;
    void delete(Long id);
    boolean isTokenValid(String token) throws ForbiddenException;
    Optional<AcValidationToken> getValidToken(String identity);
    boolean isLimitExceeded(User user);

    boolean canGetOTP(String username);
}
