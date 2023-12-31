package com.example.acl.domains.validationtokens.services

import com.example.acl.domains.users.models.entities.AcValidationToken
import com.example.acl.domains.users.repositories.AcValidationTokenRepository
import com.example.coreweb.utils.PageAttr
import com.example.coreweb.utils.PageableParams
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import java.time.Instant

interface AcValidationTokenServiceV2 {
    fun search(
        fromDate: Instant,
        toDate: Instant,
        params: PageableParams
    ): Page<AcValidationToken>
}

@Service
class AcValidationTokenServiceBean(
    private val acValidationTokenRepository: AcValidationTokenRepository
) : AcValidationTokenServiceV2 {
    override fun search(
        fromDate: Instant,
        toDate: Instant,
        params: PageableParams
    ): Page<AcValidationToken> = this.acValidationTokenRepository.search(
        fromDate, toDate,
        params.query,
        PageAttr.getPageRequest(params)
    )
}