package com.example.acl.domains.users.controllers.views.roles

import com.example.acl.domains.users.models.dtos.RoleDto
import com.example.acl.domains.users.models.mappers.RoleMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.frontend.base.AbstractBrowseView
import com.example.acl.frontend.models.Visual
import com.vaadin.flow.router.BeforeEnterEvent
import org.springframework.data.domain.Page
import java.util.*

class RoleBrowseView constructor(
	val roleService: RoleService,
	val roleMapper: RoleMapper
) : AbstractBrowseView<RoleDto>() {

	init {
		this.initialize(
			RoleDto::class.java,
			this.getRoles(0, 10, mapOf()),
			Optional.empty()
		)
	}

	fun getRoles(page: Int, size: Int, filters: Map<String, Any?>): Page<RoleDto> {
		if (filters.isEmpty())
			return this.roleService.search("", page, size).map { this.roleMapper.map(it) }
		val query = filters["query"] as String?
		val restricted = filters["restricted"] as Boolean
		return this.roleService.search(query ?: "", page, size).map { this.roleMapper.map(it) }
	}

	override fun defineColumnFields(): Map<String, Visual<RoleDto>> {
		return mapOf(
			"name" to Visual("Name",null),
			"description" to Visual("Description",null),
			"restricted" to Visual("Restricted",null)
		)
	}

	override fun beforeEnter(event: BeforeEnterEvent) {

	}

	override fun onItemPersisted(item: RoleDto?) {

	}

	override fun onFilterSubmitted(result: Map<String, Any?>) {
		this.initialize(
			RoleDto::class.java,
			this.getRoles(0, 10, result),
			Optional.empty()
		)
	}
}
