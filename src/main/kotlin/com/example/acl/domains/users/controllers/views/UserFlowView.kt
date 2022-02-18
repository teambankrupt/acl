package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractBrowseView
import com.example.acl.frontend.base.AbstractFlowView
import com.example.acl.frontend.layouts.MainLayout
import com.vaadin.flow.router.Route

@Route("/users/:username?/:action?(edit)", layout = MainLayout::class)
class UserFlowView(
	userService: UserService,
	userMapper: UserMapper
) : AbstractFlowView<UserUpdateAdminDto>() {

	init {
		val browseView = UserBrowseView(userService, userMapper)
		val formView = UserFormView(userService)

		browseView.setItemSelectionListener(object : AbstractBrowseView.ItemSelectionListener<UserUpdateAdminDto> {
			override fun onItemSelected(selected: Boolean, item: UserUpdateAdminDto?) {
				formView.onItemSelected(selected, item)
				println(selected)
				println(item)
			}
		})

		this.initialize(
			UserUpdateAdminDto::class.java,
			browseView,
			formView
		)
	}

}