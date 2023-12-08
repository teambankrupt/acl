package com.example.acl.domains.requestcredentials.services

import com.example.auth.entities.RequestCredentials
import com.example.auth.repositories.RequestCredentialsRepo
import com.example.coreweb.utils.PageAttr
import com.example.coreweb.utils.PageableParams
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

interface RequestCredentialsService{
    fun search(params: PageableParams): Page<RequestCredentials>
}

@Service
class RequestCredentialsServiceBean(
    private val credentialsRepo: RequestCredentialsRepo
): RequestCredentialsService{

    override fun search(params: PageableParams): Page<RequestCredentials> =
        this.credentialsRepo.search(params.query, PageAttr.getPageRequest(params))

}