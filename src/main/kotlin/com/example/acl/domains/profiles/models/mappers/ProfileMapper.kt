package com.example.acl.domains.profiles.models.mappers

import com.example.acl.commons.Constants
import com.example.acl.domains.profiles.models.dtos.ProfileDto
import com.example.acl.domains.profiles.models.entities.Profile
import com.example.acl.domains.users.services.UserService
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.models.mappers.BaseMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProfileMapper @Autowired constructor(
    private val userService: UserService
) : BaseMapper<Profile, ProfileDto> {

    override fun map(entity: Profile): ProfileDto {
        val dto = ProfileDto()

        dto.apply {
            this.id = entity.id
            this.createdAt = entity.createdAt
            this.updatedAt = entity.updatedAt

            this.birthday = entity.birthday
            this.photo = entity.photo
            this.gender = entity.user.gender
            this.bloodGroup = entity.bloodGroup
            this.maritalStatus = entity.maritalStatus
            this.religion = entity.religion
            this.userId = entity.user.id
            this.username = entity.user.username
            this.public = entity.public
        }

        return dto
    }

    override fun map(dto: ProfileDto, exEntity: Profile?): Profile {
        val entity = exEntity ?: Profile()

        entity.apply {
            this.user = userService.find(dto.userId)
                .orElseThrow { ExceptionUtil.notFound(Constants.Swagger.PROFILE, dto.userId) }
            this.birthday = dto.birthday
            this.photo = dto.photo
            if (dto.usePhotoAsAvatar && !dto.photo.isNullOrBlank())
                user.avatar = dto.photo
            this.user.gender = dto.gender
            this.bloodGroup = dto.bloodGroup
            this.maritalStatus = dto.maritalStatus
            this.religion = dto.religion
            this.public = dto.public
        }

        return entity
    }
}
