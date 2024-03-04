package com.example.acl.domains.users.controllers

import com.example.acl.domains.users.models.dtos.UserBriefResponse
import com.example.acl.domains.users.models.dtos.toBriefResponse
import com.example.acl.domains.users.services.UserService
import com.example.acl.routing.Route
import com.example.auth.config.security.SecurityContext
import com.example.auth.entities.User
import com.example.common.utils.ExceptionUtil
import com.example.common.utils.Validator
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Api(tags = ["Users"])
class UserController @Autowired constructor(
    private val userService: UserService,
) {

    @GetMapping(Route.V1.FIND_ME)
    fun me(): ResponseEntity<UserBriefResponse> {
        val auth = SecurityContext.getCurrentUser()
        val user = this.userService.find(auth.id).orElseThrow { ExceptionUtil.notFound(User::class.java, auth.id) }
        return ResponseEntity.ok(user.toBriefResponse())
    }

    @DeleteMapping(Route.V1.DELETE_ME)
    fun deleteMe(): ResponseEntity<UserBriefResponse> {
        val auth = SecurityContext.getCurrentUser()
        val user = this.userService.find(auth.id).orElseThrow { ExceptionUtil.notFound(User::class.java, auth.id) }
        userService.delete(user.id, true)
        return ResponseEntity.ok().build()
    }

    @PatchMapping(Route.V1.UPDATE_AVATAR)
    fun updateAvatar(@RequestParam("avatar") avatar: String): ResponseEntity<UserBriefResponse> {
        if (!Validator.isValidUrl(avatar)) return ResponseEntity.badRequest().build()
        val username = SecurityContext.getLoggedInUsername()
        var user = this.userService.findByUsername(username)
            .orElseThrow { ExceptionUtil.notFound("User doesn't exist") }
        user.avatar = avatar
        user = this.userService.save(user)
        return ResponseEntity.ok(user.toBriefResponse())
    }

}
