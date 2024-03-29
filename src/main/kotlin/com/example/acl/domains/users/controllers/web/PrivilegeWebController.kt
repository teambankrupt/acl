package com.example.acl.domains.users.controllers.web

import arrow.core.getOrElse
import arrow.core.toOption
import com.example.common.utils.ExceptionUtil
import com.example.acl.domains.users.models.dtos.PrivilegeDto
import com.example.acl.domains.users.models.mappers.PrivilegeMapper
import com.example.acl.domains.users.services.PrivilegeService
import com.example.acl.routing.Route
import com.example.common.exceptions.toArrow
import com.example.coreweb.listeners.EndpointsListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
class PrivilegeWebController @Autowired constructor(
    private val privilegeService: PrivilegeService,
    private val privilegeMapper: PrivilegeMapper
) {

    @GetMapping(Route.V1.WEB_PRIVILEGES_PAGE)
    fun privilegePage(model: Model): String {
        val privileges = this.privilegeService.findAll()


        model.addAttribute("privileges", privileges)

        val updateEndpoints = EndpointsListener.getEndpoints(RequestMethod.PATCH)
        updateEndpoints.addAll(EndpointsListener.getEndpoints(RequestMethod.PUT))
        model.addAttribute("readEndpoints", EndpointsListener.getEndpoints(RequestMethod.GET))
        model.addAttribute("createEndpoints", EndpointsListener.getEndpoints(RequestMethod.POST))
        model.addAttribute("updateEndpoints", updateEndpoints)
        model.addAttribute("deleteEndpoints", EndpointsListener.getEndpoints(RequestMethod.DELETE))

        return "material/fragments/roles/privileges"
    }

    @PostMapping(Route.V1.WEB_PRIVILEGE_CREATE)
    fun createPrivilege(@Valid @ModelAttribute privilegeDto: PrivilegeDto): String {
        val privilege = this.privilegeService.save(this.privilegeMapper.map(privilegeDto, null))
        return "redirect:/admin/privileges/${privilege.id}"
    }

    @GetMapping(Route.V1.WEB_PRIVILEGE_DETAILS_PAGE)
    fun privilegeDetailsPage(
        @PathVariable("privilege_id") privilegeId: Long,
        model: Model
    ): String {

        val selectedPrivilege = this.privilegeService.find(privilegeId).toArrow()
            .map { it.apply { this.urlAccesses = privilegeService.findAccesses(it.id) } }
            .getOrElse { throw ExceptionUtil.notFound("Privilege", privilegeId) }
        val privileges = this.privilegeService.findAll()
            .map { it.apply { this.urlAccesses = privilegeService.findAccesses(it.id) } }

        model.addAttribute("selectedPrivilege", selectedPrivilege)
        model.addAttribute("privileges", privileges)

        val updateEndpoints = EndpointsListener.getEndpoints(RequestMethod.PATCH)
        updateEndpoints.addAll(EndpointsListener.getEndpoints(RequestMethod.PUT))
        model.addAttribute("readEndpoints", EndpointsListener.getEndpoints(RequestMethod.GET))
        model.addAttribute("createEndpoints", EndpointsListener.getEndpoints(RequestMethod.POST))
        model.addAttribute("updateEndpoints", updateEndpoints)
        model.addAttribute("deleteEndpoints", EndpointsListener.getEndpoints(RequestMethod.DELETE))

        return "material/fragments/roles/privileges"
    }

    @PostMapping(Route.V1.WEB_PRIVILEGE_UPDATE)
    fun updatePrivilege(
        @PathVariable("privilege_id") privilegeId: Long,
        @Valid @ModelAttribute privilegeDto: PrivilegeDto
    ): String {
        var privilege =
            this.privilegeService.find(privilegeId).orElseThrow { ExceptionUtil.notFound("Privilege", privilegeId) }
        if (privilege.name == "ADMINISTRATION") throw ExceptionUtil.forbidden("Updating privilege ADMINISTRATION is not possible.")
        privilege = this.privilegeService.save(this.privilegeMapper.map(privilegeDto, privilege))
        return "redirect:/admin/privileges/${privilege.id}"
    }

}