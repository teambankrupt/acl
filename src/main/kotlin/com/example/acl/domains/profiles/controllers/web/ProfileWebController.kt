package com.example.acl.domains.profiles.controllers.web

import com.example.acl.domains.profiles.models.dtos.ProfileDto
import com.example.acl.domains.profiles.models.mappers.ProfileMapper
import com.example.acl.domains.profiles.services.ProfileService
import com.example.acl.routing.Route
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.domains.base.controllers.CrudWebControllerV2
import com.example.coreweb.domains.base.models.enums.SortByFields
import org.springframework.data.domain.Sort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.validation.Valid

@Controller
class ProfileWebController @Autowired constructor(
    private val profileService: ProfileService,
    private val profileMapper: ProfileMapper
) : CrudWebControllerV2<ProfileDto> {

    @GetMapping(Route.V1.ADMIN_SEARCH_PROFILES)
    override fun search(
        @RequestParam("q", defaultValue = "") query: String,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("sort_by", defaultValue = "ID") sortBy: SortByFields,
        @RequestParam("sort_direction", defaultValue = "DESC") direction: Sort.Direction,
        model: Model
    ): String {
        val entities = this.profileService.search(query, page, size, sortBy, direction)
        model.addAttribute("profiles", entities)
        return "profiles/fragments/all"
    }

    @GetMapping(Route.V1.ADMIN_FIND_PROFILE)
    override fun find(
        @PathVariable("id") id: Long,
        model: Model
    ): String {
        val entity = this.profileService.find(id).orElseThrow { ExceptionUtil.notFound("Profile", id) }
        model.addAttribute("profile", entity)
        return "profiles/fragments/details"
    }

    @GetMapping(Route.V1.ADMIN_CREATE_PROFILE_PAGE)
    override fun createPage(model: Model): String {
        return "profiles/fragments/create"
    }

    @PostMapping(Route.V1.ADMIN_CREATE_PROFILE)
    override fun create(
        @Valid @ModelAttribute dto: ProfileDto,
        redirectAttributes: RedirectAttributes
    ): String {
        val entity = this.profileService.save(this.profileMapper.map(dto, null))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.ADMIN_FIND_PROFILE.replace("{id}", entity.id.toString())}"
    }

    @GetMapping(Route.V1.ADMIN_UPDATE_PROFILE_PAGE)
    override fun updatePage(@PathVariable("id") id: Long, model: Model): String {
        val entity = this.profileService.find(id).orElseThrow { ExceptionUtil.notFound("Profile", id) }
        model.addAttribute("profile", entity)
        return "profiles/fragments/create"
    }

    @PostMapping(Route.V1.ADMIN_UPDATE_PROFILE)
    override fun update(
        @PathVariable("id") id: Long,
        @Valid @ModelAttribute dto: ProfileDto,
        redirectAttributes: RedirectAttributes
    ): String {
        var entity = this.profileService.find(id).orElseThrow { ExceptionUtil.notFound("Profile", id) }
        entity = this.profileService.save(this.profileMapper.map(dto, entity))
        redirectAttributes.addFlashAttribute("message", "Success!!")
        return "redirect:${Route.V1.ADMIN_FIND_PROFILE.replace("{id}", entity.id.toString())}"
    }

    @PostMapping(Route.V1.ADMIN_DELETE_PROFILE)
    override fun delete(
        @PathVariable("id") id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        this.profileService.delete(id, true)
        redirectAttributes.addFlashAttribute("message", "Deleted!!")
        return "redirect:${Route.V1.ADMIN_SEARCH_PROFILES}";
    }

}
