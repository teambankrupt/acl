package com.example.acl.domains.users.views.privileges

import com.example.acl.domains.users.models.dtos.PrivilegeDto
import com.example.acl.domains.users.models.mappers.PrivilegeMapper
import com.example.acl.domains.users.services.PrivilegeService
import com.example.acl.frontend.base.AbstractBrowseView
import com.example.acl.frontend.models.Visual
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.function.ValueProvider
import com.vaadin.flow.router.BeforeEnterEvent
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class PrivilegeBrowseView constructor(
	private val privilegeService: PrivilegeService, private val privilegeMapper: PrivilegeMapper
) : AbstractBrowseView<PrivilegeDto>() {

	init {
		val privileges = this.fetchPrivileges(0, 10, mapOf())
		this.initialize(
			PrivilegeDto::class.java, privileges, Optional.empty()
		)
	}

	private fun fetchPrivileges(page: Int, size: Int, filters: Map<String, Any?>): Page<PrivilegeDto> {
		val privileges = if (filters.isEmpty()) {
			this.privilegeService.findAll()
		} else {
			val query = filters["query"] as String?
			this.privilegeService.search(query ?: "", page, size)
		}
		val dtos = privileges.map { this.privilegeMapper.map(it) }
		return PageImpl(dtos)
	}

	override fun defineColumnFields(): Map<String, Visual<PrivilegeDto>>? {
		return mapOf(
			"label" to Visual("Label", null),
			"name" to Visual("Name", null),
			"description" to Visual("Description", null),
			"lastUpdated" to Visual(
				"Updated At", LocalDateRenderer(
					{ it.lastUpdated?.atZone(ZoneOffset.systemDefault())?.toLocalDate() },
					DateTimeFormatter.ofLocalizedDate(
						FormatStyle.MEDIUM
					)
				)
			)
		)
	}

	override fun beforeEnter(event: BeforeEnterEvent) {

	}

	override fun onFilterSubmitted(result: Map<String, Any?>) {
		val privileges = this.fetchPrivileges(0, 10, result)
		this.initialize(
			PrivilegeDto::class.java, privileges, Optional.empty()
		)
	}

	override fun onItemPersisted(item: PrivilegeDto?) {

	}
}
