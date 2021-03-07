package com.example.acl.domains.profiles.services.beans

import com.example.acl.commons.Constants
import com.example.acl.domains.profiles.models.entities.Profile
import com.example.acl.domains.profiles.models.enums.BloodGroup
import com.example.acl.domains.profiles.models.enums.Gender
import com.example.acl.domains.profiles.models.enums.MaritalStatus
import com.example.acl.domains.profiles.models.enums.Religion
import com.example.acl.domains.profiles.repositories.ProfileRepository
import com.example.acl.domains.profiles.services.ProfileService
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.utils.PageAttr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.util.*
import com.example.coreweb.domains.base.models.enums.SortByFields
import org.springframework.data.domain.Sort

@Service
class ProfileServiceBean @Autowired constructor(
    private val profileRepository: ProfileRepository
) : ProfileService {

    override fun search(
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
    ): Page<Profile> {
        return this.profileRepository.search(
            query.toLowerCase(),
            gender,
            bloodGroup,
            maritalStatus,
            religion,
            userId,
            username,
            contactId,
            PageAttr.getPageRequest(page, size, sortBy.fieldName, direction)
        )
    }

    override fun search(
        query: String,
        page: Int,
        size: Int,
        sortBy: SortByFields,
        direction: Sort.Direction
    ): Page<Profile> {
        return this.profileRepository.search(
            query.toLowerCase(),
            PageAttr.getPageRequest(page, size, sortBy.fieldName, direction)
        )
    }

    override fun findByUserId(id: Long): Optional<Profile> {
        return this.profileRepository.findByUserId(id)
    }

    override fun findByUserName(username: String): Optional<Profile> {
        return this.profileRepository.findByUserName(username)
    }

    override fun save(entity: Profile): Profile {
        this.validate(entity)
        return this.profileRepository.save(entity)
    }

    override fun find(id: Long): Optional<Profile> {
        return this.profileRepository.find(id)
    }

    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val entity = this.find(id).orElseThrow { ExceptionUtil.notFound(Constants.Swagger.PROFILE, id) }
            entity.isDeleted = true
            this.profileRepository.save(entity)
        }
        this.profileRepository.deleteById(id)
    }

    override fun validate(entity: Profile) {

    }
}
