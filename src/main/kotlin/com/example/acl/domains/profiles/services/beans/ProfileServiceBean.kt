package com.example.acl.domains.profiles.services.beans

import com.example.acl.commons.Constants
import com.example.acl.domains.profiles.models.entities.Profile
import com.example.acl.domains.profiles.models.enums.BloodGroup
import com.example.acl.domains.profiles.models.enums.MaritalStatus
import com.example.acl.domains.profiles.models.enums.Religion
import com.example.acl.domains.profiles.repositories.ProfileRepository
import com.example.acl.domains.profiles.services.ProfileService
import com.example.auth.repositories.UserRepo
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.utils.PageAttr
import com.example.coreweb.utils.PageableParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class ProfileServiceBean @Autowired constructor(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepo
) : ProfileService {

    override fun search(
        bloodGroup: BloodGroup?,
        maritalStatus: MaritalStatus?,
        religion: Religion?,
        userId: Long?,
        username: String?,
        params: PageableParams
    ): Page<Profile> {
        return this.profileRepository.search(
            params.query,
            bloodGroup,
            maritalStatus,
            religion,
            userId,
            username,
            PageAttr.getPageRequest(params)
        )
    }

    override fun search(
        params: PageableParams
    ): Page<Profile> {
        return this.profileRepository.search(
            params.query,
            PageAttr.getPageRequest(params)
        )
    }

    override fun findByUserId(id: Long): Optional<Profile> {
        return this.profileRepository.findByUserId(id)
    }

    override fun findByUsername(username: String): Optional<Profile> {
        return this.profileRepository.findByUsername(username)
    }

    @Transactional
    override fun save(entity: Profile): Profile {
        this.validate(entity)
        val user = this.userRepository.save(entity.user)
        entity.user = user
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
            return
        }
        this.profileRepository.deleteById(id)
    }

    override fun validate(entity: Profile) {

    }
}
