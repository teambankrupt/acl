package com.example.acl.domains.users.controllers

import com.example.acl.domains.users.services.UserService
import com.example.acl.routing.Route
import com.example.common.utils.ExceptionUtil
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
@Api(tags = ["Users"])
class PublicUserController @Autowired constructor(
    private val userService: UserService
) {

    @GetMapping(Route.V1.FIND_AVATAR_URL)
    fun getPhoto(@PathVariable("username") username: String): String {
        val user = this.userService.findByUsername(username)
            .orElseThrow { ExceptionUtil.notFound("Couldn't find user with username: $username") }
        return "redirect:" + user.avatar
    }

}
