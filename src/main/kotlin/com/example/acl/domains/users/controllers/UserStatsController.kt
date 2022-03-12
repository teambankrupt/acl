package com.example.acl.domains.users.controllers

import com.example.acl.commons.Constants
import com.example.acl.domains.users.services.UserService
import com.example.acl.routing.Route
import com.example.common.utils.DateUtil
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @project IntelliJ IDEA
 * @author mir00r on 1/3/22
 */
@RestController
@Api(tags = [Constants.Swagger.USERS_STATISTICS], description = Constants.Swagger.REST_API)
class UserStatsController @Autowired constructor(
    private val userService: UserService
) {

    @GetMapping(Route.V1.ADMIN_USERS_GROWTH_STATS)
    fun getGrowthStats(@RequestParam("period") period: DateUtil.Periods): ResponseEntity<LinkedHashMap<String, Any>> {

        val stats = this.userService.getGrowthStats(period)
        return ResponseEntity.ok(stats)
    }
}
