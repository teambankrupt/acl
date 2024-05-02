package com.example.acl.domains.users.models.dtos

import com.example.auth.entities.Privilege
import com.example.auth.entities.UrlAccess
import com.example.auth.enums.AccessLevels
import com.fasterxml.jackson.annotation.JsonProperty

data class PrivilegeBriefResponse(
    val name: String,

    val description: String?,

    val label: String
)

fun Privilege.toBriefResponse() = PrivilegeBriefResponse(
    name = this.name,
    description = this.description,
    label = this.label
)

data class PrivilegeDetailResponse(
    val name: String,

    val description: String?,

    val label: String,

    @field:JsonProperty("urls_all_access")
    val urlsAllAccess: List<String>,

    @field:JsonProperty("urls_read_access")
    val urlsReadAccess: List<String>,

    @field:JsonProperty("urls_create_access")
    val urlsCreateAccess: List<String>,

    @field:JsonProperty("urls_update_access")
    val urlsUpdateAccess: List<String>,

    @field:JsonProperty("urls_delete_access")
    val urlsDeleteAccess: List<String>,
)

fun Privilege.toDetailResponse(
    getAccesses: (privilegeId: Long) -> List<UrlAccess>
) = PrivilegeDetailResponse(
    name = this.name,
    description = this.description,
    label = this.label,
    urlsAllAccess = getAccesses(this.id)
        .filter { it.accessLevel == AccessLevels.ALL }
        .map { it.url },
    urlsCreateAccess = getAccesses(this.id)
        .filter { it.accessLevel == AccessLevels.CREATE }
        .map { it.url },
    urlsReadAccess = getAccesses(this.id)
        .filter { it.accessLevel == AccessLevels.READ }
        .map { it.url },
    urlsUpdateAccess = getAccesses(this.id)
        .filter { it.accessLevel == AccessLevels.UPDATE }
        .map { it.url },
    urlsDeleteAccess = getAccesses(this.id)
        .filter { it.accessLevel == AccessLevels.DELETE }
        .map { it.url },
)