package com.example.acl.domains.users.services.beans

import com.example.acl.domains.users.repositories.PrivilegeRepository
import com.example.acl.domains.users.repositories.UrlAccessRepository
import com.example.acl.domains.users.services.PrivilegeService
import com.example.auth.entities.Privilege
import com.example.auth.entities.UrlAccess
import com.example.common.exceptions.exists.AlreadyExistsException
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.utils.PageAttr
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
open class PrivilegeServiceImpl @Autowired constructor(
    private val privilegeRepo: PrivilegeRepository,
    private val urlAccessRepository: UrlAccessRepository
) : PrivilegeService {

    override fun empty(): Boolean {
        return this.privilegeRepo.count() == 0L
    }

    override fun findAll(): List<Privilege> {
        return this.privilegeRepo.findAll()
    }

    override fun findAccesses(privilegeId: Long): List<UrlAccess> =
        this.urlAccessRepository.findByPrivilege(privilegeId)

    override fun find(name: String): Optional<Privilege> {
        return this.privilegeRepo.find(name)
    }

    @Transactional
    override fun save(entity: Privilege): Privilege {
        // When creating new privilege check if already exists with same name
        if (entity.id == null) {
            if (this.privilegeRepo.existsByName(entity.name)
                || this.privilegeRepo.existsByLabel(entity.label)
            )
                throw AlreadyExistsException("Privilege with same name or label already exists!")
        }
        val privilege = this.privilegeRepo.save(entity)

        this.urlAccessRepository.deleteByPrivilegeId(privilege.id)
        this.urlAccessRepository.saveAll(entity.urlAccesses.map { UrlAccess(privilege, it.accessLevel, it.url) })

        return find(privilege.id).orElseThrow { ExceptionUtil.invalid("Couldn't save privilege") }
    }

    override fun find(id: Long): Optional<Privilege> {
        return this.privilegeRepo.find(id)
    }

    override fun search(query: String, page: Int, size: Int): Page<Privilege> {
        return this.privilegeRepo.search(query, PageAttr.getPageRequest(page, size))
    }


    override fun delete(id: Long, softDelete: Boolean) {
        if (softDelete) {
            val privilege = this.privilegeRepo.find(id).orElseThrow { ExceptionUtil.notFound("Privilege", id) }
            privilege.isDeleted = true
            this.privilegeRepo.save(privilege)
            return
        }
        this.privilegeRepo.deleteById(id)
    }

}
