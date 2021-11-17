package com.example.acl.domains.users.controllers

import com.example.acl.domains.users.models.dtos.UserResponse
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.UserService
import com.example.acl.routing.Route
import com.example.auth.config.security.SecurityContext
import com.example.auth.entities.User
import com.example.common.utils.ExceptionUtil
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["Users"])
class UserController @Autowired constructor(
    private val userService: UserService,
    private val userMapper: UserMapper
) {

    @GetMapping(Route.V1.FIND_ME)
    fun me(): ResponseEntity<UserResponse> {
        val auth = SecurityContext.getCurrentUser()
        val user = this.userService.find(auth.id).orElseThrow { ExceptionUtil.notFound(User::class.java, auth.id) }
        return ResponseEntity.ok(this.userMapper.map(user))
    }

}
