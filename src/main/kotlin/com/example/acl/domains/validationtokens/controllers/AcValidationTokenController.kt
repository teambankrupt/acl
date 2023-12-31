package com.example.acl.domains.validationtokens.controllers

import com.example.acl.domains.validationtokens.models.AcValidationTokenResponse
import com.example.acl.domains.validationtokens.models.toDto
import com.example.acl.domains.validationtokens.services.AcValidationTokenServiceV2
import com.example.acl.routing.Route
import com.example.coreweb.domains.base.models.enums.SortByFields
import com.example.coreweb.utils.PageableParams
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class AcValidationTokenController(
    private val acValidationTokenService: AcValidationTokenServiceV2
) {

    @GetMapping(Route.V1.ADMIN_GET_VALIDATION_TOKENS)
    fun getTokens(
        @RequestParam("from_date") fromDate: Instant?,
        @RequestParam("to_date") toDate: Instant?,
        @RequestParam("q", required = false) query: String?,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction
    ): ResponseEntity<Page<AcValidationTokenResponse>> = ResponseEntity.ok(
        this.acValidationTokenService.search(
            fromDate = fromDate ?: Instant.EPOCH,
            toDate = toDate ?: Instant.now(),
            params = PageableParams.of(query, page, size, sortBy, direction)
        ).map { it.toDto() }
    )

}