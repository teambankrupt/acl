package com.example.acl.domains.users.controllers.views.roles

import com.example.acl.domains.users.models.dtos.RoleDto
import com.example.acl.domains.users.models.mappers.RoleMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.frontend.base.AbstractBrowseView
import com.vaadin.flow.router.BeforeEnterEvent
import java.util.*

class RoleBrowseView constructor(
	val roleService: RoleService,
	val roleMapper: RoleMapper
) : AbstractBrowseView<RoleDto>() {

	init {
		val roles = this.roleService.search("",0,100)

		this.initialize(
			RoleDto::class.java,
			roles.map { this.roleMapper.map(it) },
			Optional.empty()
		)
	}

	override fun defineColumnFields(): Map<String, String>? {
		return mapOf(
			"name" to "Name",
			"description" to "Description",
			"restricted" to "Restricted"
		)
	}

	override fun beforeEnter(event: BeforeEnterEvent) {

	}

	override fun onItemPersisted(item: RoleDto?) {

	}

	override fun onFilterSubmitted(result: Map<String, Any?>) {
		TODO("Not yet implemented")
	}
}