package com.example.acl.domains.users.models.dtos

import com.example.auth.entities.Role

data class RoleBriefResponse(
    val name: String,
    val description: String?,
    val restricted: Boolean,
    val privileges: List<PrivilegeBriefResponse>
)

fun Role.toBriefResponse() = RoleBriefResponse(
    name = this.name,
    description = this.description,
    restricted = this.isRestricted,
    privileges = privileges.map {
        it.toBriefResponse()
    }
)