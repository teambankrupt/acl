package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractMasterDetailView
import com.example.acl.frontend.layouts.MainLayout
import com.example.auth.entities.User
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.Route
import java.util.*

@Route("/users/:username?/:action?(edit)", layout = MainLayout::class)
class UserMasterDetail(
	private val userService: UserService,
	private val userMapper: UserMapper
) : AbstractMasterDetailView<UserUpdateAdminDto>() {
	private val EDIT_ROUTE_TEMPLATE = "/users/%s/edit"

	override fun beforeEnter(event: BeforeEnterEvent) {
		val users = this.userService.findAll(0)
		val dtos = users.map { this.userMapper.mapToAdminDto(it) }
		val username = event.routeParameters.get("username")
		val user: Optional<User> =
			if (username.isPresent) this.userService.findByUsername(username.get()) else Optional.empty()
		val dto: Optional<UserUpdateAdminDto> = user.map { this.userMapper.mapToAdminDto(it) }
		this.initialize(
			UserUpdateAdminDto::class.java, dtos, dto,
//			mapOf(
//				"avatar" to "Avatar",
//				"name" to "Name",
//				"username" to "Username",
//				"gender" to "Gender",
//				"email" to "Email",
//				"phone" to "Phone"
//			),
//			mapOf(
//				"avatar" to "Avatar",
//				"name" to "Name",
//				"username" to "Username",
//				"gender" to "Gender",
//				"email" to "Email",
//				"phone" to "Phone"
//			),
		)
	}

	override fun onRowSelected(event: AbstractField.ComponentValueChangeEvent<Grid<UserUpdateAdminDto>, UserUpdateAdminDto>) {
		val user: UserUpdateAdminDto = event.value
		UI.getCurrent().navigate(String.format(EDIT_ROUTE_TEMPLATE, user.username))
	}

	override fun onRowUnselected(event: AbstractField.ComponentValueChangeEvent<Grid<UserUpdateAdminDto>, UserUpdateAdminDto>) {
//		clearForm()
		UI.getCurrent().navigate(UserMasterDetail::class.java)
	}

}