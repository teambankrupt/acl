package com.example.acl.domains.users.controllers.admin

import com.example.acl.domains.users.models.dtos.PrivilegeDto
import com.example.acl.domains.users.models.mappers.PrivilegeMapper
import com.example.acl.domains.users.services.PrivilegeService
import com.example.common.exceptions.notfound.NotFoundException
import com.example.coreweb.commons.Constants
import com.example.coreweb.domains.base.controllers.CrudController
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/admin/privileges")
@Api(tags = [Constants.Swagger.PRIVILEGES_ADMIN], description = Constants.Swagger.PRIVILEGES_ADMIN_API_DETAILS)
class PrivilegeAdminController @Autowired constructor(
    private val privilegeService: PrivilegeService,
    private val privilegeMapper: PrivilegeMapper
) : CrudController<PrivilegeDto> {

    @GetMapping("")
    override fun search(
        @RequestParam("query", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int
    ): ResponseEntity<Page<PrivilegeDto>> {
        val privileges = this.privilegeService.search(query, page, size)
        fun accesses(privilegeId: Long) = this.privilegeService.findAccesses(privilegeId)
        return ResponseEntity.ok(privileges.map {
            this.privilegeMapper.map(it, accesses(it.id))
        })
    }

    @PostMapping("")
    override fun create(@RequestBody dto: PrivilegeDto): ResponseEntity<PrivilegeDto> {
        val privilege = this.privilegeService.save(this.privilegeMapper.map(dto, null))
        fun accesses(privilegeId: Long) = this.privilegeService.findAccesses(privilegeId)
        return ResponseEntity.ok(this.privilegeMapper.map(privilege, accesses(privilege.id)))
    }

    @GetMapping("/{id}")
    override fun find(@PathVariable id: Long): ResponseEntity<PrivilegeDto> {
        val privilege =
            this.privilegeService.find(id).orElseThrow { NotFoundException("Couldn't find privilege with id: $id") }

        fun accesses(privilegeId: Long) = this.privilegeService.findAccesses(privilegeId)
        return ResponseEntity.ok(this.privilegeMapper.map(privilege, accesses(privilege.id)))
    }


    @PatchMapping("/{id}")
    override fun update(
        @PathVariable id: Long,
        @RequestBody dto: PrivilegeDto
    ): ResponseEntity<PrivilegeDto> {
        var privilege = this.privilegeService.find(id)
            .orElseThrow { NotFoundException("Could not find privilege with id: $id") }
        privilege = this.privilegeService.save(this.privilegeMapper.map(dto, privilege))
        fun accesses(privilegeId: Long) = this.privilegeService.findAccesses(privilegeId)
        return ResponseEntity.ok(this.privilegeMapper.map(privilege, accesses(privilege.id)))
    }

    @DeleteMapping("/{id}")
    override fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        this.privilegeService.delete(id, true)
        return ResponseEntity.ok().build()
    }

}
