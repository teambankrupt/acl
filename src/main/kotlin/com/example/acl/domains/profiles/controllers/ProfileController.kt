package com.example.acl.domains.profiles.controllers

import com.example.acl.commons.Constants
import com.example.acl.domains.profiles.models.dtos.ProfileDto
import com.example.acl.domains.profiles.models.enums.BloodGroup
import com.example.acl.domains.profiles.models.enums.MaritalStatus
import com.example.acl.domains.profiles.models.enums.Religion
import com.example.acl.domains.profiles.models.mappers.ProfileMapper
import com.example.acl.domains.profiles.services.ProfileService
import com.example.acl.routing.Route
import com.example.auth.config.security.SecurityContext
import com.example.auth.enums.Genders
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.controllers.CrudControllerV2
import com.example.coreweb.domains.base.models.enums.SortByFields
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import org.springframework.data.domain.Sort

@RestController
@Api(tags = [Constants.Swagger.PROFILE], description = Constants.Swagger.REST_API)
class ProfileController @Autowired constructor(
    private val profileService: ProfileService,
    private val profileMapper: ProfileMapper
) : CrudControllerV2<ProfileDto> {

    @GetMapping(Route.V1.SEARCH_PROFILES)
    fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("blood_group", required = false) bloodGroup: BloodGroup?,
        @RequestParam("marital_status", required = false) maritalStatus: MaritalStatus?,
        @RequestParam("religion", required = false) religion: Religion?,
        @RequestParam("user_id", required = false) userId: Long?,
        @RequestParam("username", required = false) username: String?,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction
    ): ResponseEntity<Page<ProfileDto>> {

        val entities = this.profileService.search(
            query,
            page,
            size,
            bloodGroup,
            maritalStatus,
            religion,
            userId,
            username,
            sortBy,
            direction
        )
        return ResponseEntity.ok(entities.map { this.profileMapper.map(it) })
    }

    //    @GetMapping(Route.V1.SEARCH_PROFILES)
    override fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction
    ): ResponseEntity<Page<ProfileDto>> {
        val entities = this.profileService.search(query, page, size, sortBy, direction)
        return ResponseEntity.ok(entities.map { this.profileMapper.map(it) })
    }

    @GetMapping(Route.V1.FIND_PROFILE)
    override fun find(@PathVariable("id") id: Long): ResponseEntity<ProfileDto> {
        val entity = this.profileService.find(id).orElseThrow { ExceptionUtil.notFound(Constants.Swagger.PROFILE, id) }
        return ResponseEntity.ok(this.profileMapper.map(entity))
    }

    @GetMapping(Route.V1.MY_PROFILE)
    fun myProfile(): ResponseEntity<ProfileDto> {
        val auth = SecurityContext.getCurrentUser()
        val entity = this.profileService.findByUsername(auth.username)
            .orElseThrow { ExceptionUtil.notFound("No profile found with username: ${auth.username}") }
        return ResponseEntity.ok(this.profileMapper.map(entity))
    }

    @PostMapping(Route.V1.CREATE_PROFILE)
    override fun create(@Valid @RequestBody dto: ProfileDto): ResponseEntity<ProfileDto> {
        val entity = this.profileService.save(this.profileMapper.map(dto, null))
        return ResponseEntity.ok(this.profileMapper.map(entity))
    }

    @PatchMapping(Route.V1.UPDATE_PROFILE)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @RequestBody dto: ProfileDto
    ): ResponseEntity<ProfileDto> {
        var entity = this.profileService.find(id).orElseThrow { ExceptionUtil.notFound(Constants.Swagger.PROFILE, id) }
        entity = this.profileService.save(this.profileMapper.map(dto, entity))
        return ResponseEntity.ok(this.profileMapper.map(entity))
    }

    @DeleteMapping(Route.V1.DELETE_PROFILE)
    override fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        this.profileService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}
