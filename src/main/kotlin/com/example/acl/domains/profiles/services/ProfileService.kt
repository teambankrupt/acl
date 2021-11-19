package com.example.acl.domains.profiles.services

import com.example.acl.domains.profiles.models.entities.Profile
import com.example.acl.domains.profiles.models.enums.BloodGroup
import com.example.acl.domains.profiles.models.enums.MaritalStatus
import com.example.acl.domains.profiles.models.enums.Religion
import com.example.coreweb.domains.base.services.CrudServiceV3
import com.example.coreweb.utils.PageableParams
import org.springframework.data.domain.Page
import java.util.*

interface ProfileService : CrudServiceV3<Profile> {
    fun search(
        bloodGroup: BloodGroup?,
        maritalStatus: MaritalStatus?,
        religion: Religion?,
        userId: Long?,
        username: String?,
        params: PageableParams
    ): Page<Profile>

    fun findByUserId(id: Long): Optional<Profile>
    fun findByUsername(username: String): Optional<Profile>
}
