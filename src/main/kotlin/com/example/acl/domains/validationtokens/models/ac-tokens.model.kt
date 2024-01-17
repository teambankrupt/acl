package com.example.acl.domains.validationtokens.models

import com.example.acl.domains.users.models.dtos.UserBriefResponse
import com.example.acl.domains.users.models.dtos.toResponse
import com.example.acl.domains.users.models.entities.AcValidationToken
import com.example.acl.domains.users.models.enums.AuthMethods
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class AcValidationTokenResponse(
    val id: Long,

    @JsonProperty("created_at")
    val createdAt: Instant,

    @JsonProperty("updated_at")
    val updatedAt: Instant?,

    val token: String,

    @JsonProperty("is_token_valid")
    val tokenValid: Boolean,

    @JsonProperty("token_valid_until")
    val tokenValidUntil: Instant,

    val user: UserBriefResponse?,

    val reason: String,

    @JsonProperty("identity")
    val identity: String,

    @JsonProperty("registration_method")
    val registrationMethod: AuthMethods?
)

fun AcValidationToken.toDto() = AcValidationTokenResponse(
    id = this.id,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    token = this.token,
    tokenValid = this.isTokenValid(),
    tokenValidUntil = this.tokenValidUntil,
    user = this.user?.toResponse(),
    reason = this.reason,
    identity = this.username,
    registrationMethod = this.registrationMethod
)