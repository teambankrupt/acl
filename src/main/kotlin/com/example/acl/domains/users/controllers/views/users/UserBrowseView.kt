package com.example.acl.domains.users.controllers.views.users

import com.example.acl.domains.users.models.dtos.RoleDto
import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractBrowseView
import com.example.acl.frontend.models.Visual
import com.example.auth.entities.User
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.data.renderer.ClickableRenderer
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.IconRenderer
import com.vaadin.flow.data.renderer.NativeButtonRenderer
import com.vaadin.flow.router.BeforeEnterEvent
import org.springframework.data.domain.Page
import java.util.*

class UserBrowseView(
	private val userService: UserService,
	private val userMapper: UserMapper
) : AbstractBrowseView<UserUpdateAdminDto>() {
	private var event: BeforeEnterEvent? = null

	init {
		val dtos = this.getUsers(0, 10, mapOf())
		val username = event?.routeParameters?.get("username") ?: Optional.empty()
		val user: Optional<User> =
			if (username.isPresent) this.userService.findByUsername(username.get()) else Optional.empty()
		val dto: Optional<UserUpdateAdminDto> = user.map { this.userMapper.mapToAdminDto(it) }

		this.initialize(
			UserUpdateAdminDto::class.java, dtos, dto
		)
	}

	fun getUsers(page: Int, size: Int, filters: Map<String, Any?>): Page<UserUpdateAdminDto> {
		val users: Page<User>
		users = if (filters.isEmpty())
			this.userService.findAll(page)
		else {
			val role = filters["role"] as String?
			val query = filters["query"] as String?
			this.userService.search(query ?: "", role, page, size)
		}
		return users.map { this.userMapper.mapToAdminDto(it) }
	}

	override fun beforeEnter(event: BeforeEnterEvent) {
		this.event = event
	}

	override fun defineColumnFields(): Map<String, Visual<UserUpdateAdminDto>> {
		return mapOf(
			"avatar" to Visual(
				"Avatar",
				ComponentRenderer({ Image() }, { img: Image, it -> if (it.avatar != null) img.src = it.avatar })
			),
			"name" to Visual("Name", null),
			"username" to Visual("Username", null),
			"gender" to Visual("Gender", null),
			"email" to Visual("Email", null),
			"phone" to Visual("Phone", null),
			"enabled" to Visual("Enabled", null),
			"accountNonLocked" to Visual("Account Non Locked", null),
			"accountNonExpired" to Visual("Account Non Expired", null),
			"credentialsNonExpired" to Visual("Credentials Non Expired", null)
		)
	}

	override fun onItemPersisted(item: UserUpdateAdminDto?) {
		UI.getCurrent().page.reload()
	}

	override fun onFilterSubmitted(result: Map<String, Any?>) {
		val users = this.getUsers(0, 10, result)
		this.initialize(
			UserUpdateAdminDto::class.java, users, Optional.empty()
		)
	}


}
