package com.example.acl.domains.users.models.dtos

import com.example.auth.entities.User
import com.example.auth.enums.Genders
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class UserBriefResponse(
    val id: Long = 0,

    @field:JsonProperty("created_at")
    val createdAt: Instant,

    @field:JsonProperty("updated_at")
    val updatedAt: Instant? = null,

    val name: String,

    val username: String,

    val phone: String?,

    val email: String?,

    val gender: Genders,

    val roles: List<Long>,

    val avatar: String?,

    val label: String,
)

fun User.toBriefResponse(): UserBriefResponse = UserBriefResponse(
    id = this.id,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    name = this.name,
    username = this.username,
    phone = this.phone,
    email = this.email,
    gender = this.gender,
    roles = this.roles.map { it.id },
    avatar = this.avatar,
    label = "${this.name.trim()} (${this.username})",
)

data class UserDetailResponse(
    val id: Long = 0,

    @field:JsonProperty("created_at")
    val createdAt: Instant,

    @field:JsonProperty("updated_at")
    val updatedAt: Instant? = null,

    val name: String,

    val username: String,

    val phone: String?,

    val email: String?,

    val gender: Genders,

    @field:JsonProperty("time_zone")
    val timeZone: String,

    val roles: List<RoleBriefResponse>,

    val avatar: String?,

    val label: String,
)

fun User.toDetailResponse(): UserDetailResponse = UserDetailResponse(
    id = this.id,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    name = this.name,
    username = this.username,
    phone = this.phone,
    email = this.email,
    gender = this.gender,
    timeZone = this.timeZone,
    roles = this.roles.map { it.toBriefResponse() },
    avatar = this.avatar,
    label = "${this.name.trim()} (${this.username})",
)
