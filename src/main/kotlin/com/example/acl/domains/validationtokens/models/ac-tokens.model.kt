package com.example.acl.domains.validationtokens.models

import com.example.acl.domains.users.models.dtos.UserResponse
import com.example.acl.domains.users.models.dtos.toResponse
import com.example.acl.domains.users.models.entities.AcValidationToken
import com.example.acl.domains.users.models.enums.AuthMethods
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class AcValidationTokenDto(
    val token: String,
    @JsonProperty("is_token_valid")
    val tokenValid: Boolean,
    @JsonProperty("token_valid_until")
    val tokenValidUntil: Instant,
    val user: UserResponse?,
    val reason: String,
    val username: String,
    @JsonProperty("registration_method")
    val registrationMethod: AuthMethods?
)

fun AcValidationToken.toDto() = AcValidationTokenDto(
    token = this.token,
    tokenValid = this.isTokenValid(),
    tokenValidUntil = this.tokenValidUntil,
    user = this.user?.toResponse(),
    reason = this.reason,
    username = this.username,
    registrationMethod = this.registrationMethod
)