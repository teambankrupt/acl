package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractFlowView
import com.example.acl.frontend.layouts.MainLayout
import com.vaadin.flow.router.Route

@Route("/users/:username?/:action?(edit)", layout = MainLayout::class)
class UserFlowView(
	userService: UserService,
	userMapper: UserMapper
) : AbstractFlowView<UserUpdateAdminDto>() {

	init {
		this.initialize(
			UserUpdateAdminDto::class.java,
			UserBrowseView(userService, userMapper),
			UserFormView(userService)
		)
	}

}