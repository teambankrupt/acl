package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractBrowseView
import com.example.auth.entities.User
import com.vaadin.flow.component.UI
import com.vaadin.flow.router.BeforeEnterEvent
import java.util.*

class UserBrowseView(
	private val userService: UserService,
	private val userMapper: UserMapper
) : AbstractBrowseView<UserUpdateAdminDto>() {
	private var event: BeforeEnterEvent? = null

	init {
		val users = this.userService.findAll(0)
		val dtos = users.map { this.userMapper.mapToAdminDto(it) }
		val username = event?.routeParameters?.get("username") ?: Optional.empty()
		val user: Optional<User> =
			if (username.isPresent) this.userService.findByUsername(username.get()) else Optional.empty()
		val dto: Optional<UserUpdateAdminDto> = user.map { this.userMapper.mapToAdminDto(it) }

		this.initialize(
			UserUpdateAdminDto::class.java, dtos, dto
		)
	}

	override fun beforeEnter(event: BeforeEnterEvent) {
		this.event = event
	}

	override fun defineColumnFields(): Map<String, String>? {
		return mapOf(
			"avatar" to "Avatar",
			"name" to "Name",
			"username" to "Username",
			"gender" to "Gender",
			"email" to "Email",
			"phone" to "Phone",
			"enabled" to "Enabled",
			"accountNonLocked" to "Account Non Locked",
			"accountNonExpired" to "Account Non Expired",
			"credentialsNonExpired" to "Credentials Non Expired"
		)
	}

	override fun onItemPersisted(item: UserUpdateAdminDto?) {
		UI.getCurrent().page.reload()
	}


}