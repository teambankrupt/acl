package com.example.acl.domains.requestcredentials.controllers

import com.example.acl.domains.requestcredentials.services.RequestCredentialsService
import com.example.acl.routing.Route
import com.example.auth.models.RequestCredentialsRecord
import com.example.auth.models.mappers.RequestCredentialsMapper
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.utils.PageableParams
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RequestCredentialsController(
    private val credentialsService: RequestCredentialsService,
    private val credentialsMapper: RequestCredentialsMapper
) {

    @GetMapping(Route.V1.ADMIN_GET_LOCKOUTS)
    fun getLockouts(
        @RequestParam("query", required = false) query: String?,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction,
    ): ResponseEntity<Page<RequestCredentialsRecord>> {
        val rCreds = this.credentialsService.search(PageableParams.of(query, page, size, sortBy, direction))
        return ResponseEntity.ok(rCreds.map { this.credentialsMapper.toRecord(it) })
    }

}