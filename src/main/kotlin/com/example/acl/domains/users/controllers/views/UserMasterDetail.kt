package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractMasterDetailView
import com.example.acl.frontend.layouts.MainLayout
import com.example.auth.entities.User
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.Route
import java.util.*

@Route("/users/:username", layout = MainLayout::class)
class UserMasterDetail(
	private val userService: UserService
) : AbstractMasterDetailView<User>() {

	override fun beforeEnter(event: BeforeEnterEvent) {
		val users = this.userService.findAll(0)
		val username = event.routeParameters.get("username")
		val user: Optional<User> =
			if (username.isPresent) this.userService.findByUsername(username.get()) else Optional.empty()
		this.initialize(User::class.java,users, user, mapOf(
			"avatar" to "Avatar",
			"name" to "Name",
			"username" to "Username",
			"gender" to "Gender",
			"email" to "Email",
			"phone" to "Phone"
		))
	}
}