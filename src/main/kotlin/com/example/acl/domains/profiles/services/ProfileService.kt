package com.example.acl.domains.profiles.services

import com.example.acl.domains.profiles.models.entities.Profile
import com.example.acl.domains.profiles.models.enums.BloodGroup
import com.example.acl.domains.profiles.models.enums.Gender
import com.example.acl.domains.profiles.models.enums.MaritalStatus
import com.example.acl.domains.profiles.models.enums.Religion
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.domains.base.services.CrudServiceV2
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import java.util.*

interface ProfileService : CrudServiceV2<Profile> {
    fun search(
        query: String,
        page: Int,
        size: Int,
        gender: Gender?,
        bloodGroup: BloodGroup?,
        maritalStatus: MaritalStatus?,
        religion: Religion?,
        userId: Long?,
        username: String?,
        contactId: Long?,
        sortBy: SortByFields,
        direction: Sort.Direction
    ): Page<Profile>

    fun findByUserId(id: Long): Optional<Profile>
    fun findByUserName(username: String): Optional<Profile>
}
