package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractBrowseView
import com.example.auth.entities.User
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
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

	override fun onRowSelected(event: AbstractField.ComponentValueChangeEvent<Grid<UserUpdateAdminDto>, UserUpdateAdminDto>) {
		val user: UserUpdateAdminDto = event.value
		UI.getCurrent().navigate(String.format("/users/%s/edit", user.username))
	}

	override fun onRowUnselected(event: AbstractField.ComponentValueChangeEvent<Grid<UserUpdateAdminDto>, UserUpdateAdminDto>) {
		//		clearForm()
		UI.getCurrent().navigate(UserFlowView::class.java)
	}

	override fun beforeEnter(event: BeforeEnterEvent) {
		this.event = event
	}

	override fun defineColumnFields(): Map<String, String>? {
//		return mapOf(
//				"avatar" to "Avatar",
//				"name" to "Name",
//				"username" to "Username",
//				"gender" to "Gender",
//				"email" to "Email",
//				"phone" to "Phone"
//			)
		return null
	}


}