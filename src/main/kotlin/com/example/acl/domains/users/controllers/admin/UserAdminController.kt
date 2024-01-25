package com.example.acl.domains.users.controllers.admin

import com.example.acl.domains.users.models.dtos.toResponse
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.UserService
import com.example.auth.config.security.TokenService
import com.example.auth.entities.UserAuth
import com.example.common.exceptions.notfound.UserNotFoundException
import com.example.coreweb.commons.Constants
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/users")
@Api(tags = [Constants.Swagger.USERS_ADMIN], description = Constants.Swagger.USERS_ADMIN_API_DETAILS)
class UserAdminController @Autowired constructor(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val tokenService: TokenService
) {

    @GetMapping("")
    fun search(
        @RequestParam("q", required = false) query: String?,
        @RequestParam(value = "role", required = false) role: String?,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("slice", defaultValue = "false") slice: Boolean
    ): ResponseEntity<Any> {

        val userPage = this.userService.search(query, role, page, size)
        return if (slice)
            ResponseEntity.ok(userPage.map { this.userMapper.mapToSlice(it) })
        else
            ResponseEntity.ok(userPage.map { it.toResponse() })
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable("id") userId: Long): ResponseEntity<Any> {
        val user =
            this.userService.find(userId).orElseThrow { UserNotFoundException("Could not find user with id: $userId") }
        return ResponseEntity.ok(user.toResponse())
    }


    @PostMapping("/{id}/access/toggle")
    fun disableUser(
        @PathVariable("id") id: Long,
        @RequestParam("enabled") enabled: Boolean
    ): ResponseEntity<Any> {
        var user = this.userService.find(id).orElseThrow { UserNotFoundException("Could not find user with id: $id") }
        user.isEnabled = enabled
        user = this.userService.save(user)
        this.tokenService.revokeAuthentication(UserAuth(user))
        return ResponseEntity.ok(user.toResponse())
    }

    @PutMapping("/{id}/change_role")
    fun changeRole(
        @PathVariable("id") id: Long,
        @RequestParam("roles") roles: List<Long>
    ): ResponseEntity<*> {
        val user = this.userService.setRoles(id, roles)
        return ResponseEntity.ok(user.toResponse())
    }

    @PatchMapping("/{id}/changePassword")
    fun changePassword(
        @PathVariable("id") userId: Long,
        @RequestParam("newPassword") newPassword: String
    ): ResponseEntity<Any> {
        this.userService.setPassword(userId, newPassword)
        return ResponseEntity.ok().build()
    }


}
