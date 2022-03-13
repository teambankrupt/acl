package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractBrowseView
import com.example.acl.frontend.base.AbstractFlowView
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.base.AbstractFormViewV2
import com.example.acl.frontend.layouts.MainLayout
import com.example.cms.domains.fileuploads.services.FileUploadService
import com.vaadin.flow.router.Route

@Route("/users/:username?/:action?(edit)", layout = MainLayout::class)
class UserFlowView(
	userService: UserService,
	userMapper: UserMapper,
	roleService: RoleService,
	uploadService: FileUploadService
) : AbstractFlowView<UserUpdateAdminDto>() {

	init {
		val browseView = UserBrowseView(userService, userMapper)
		val formView = UserFormViewV2(userService, userMapper, roleService, uploadService)

		browseView.setItemSelectionListener(object : AbstractBrowseView.ItemSelectionListener<UserUpdateAdminDto> {
			override fun onItemSelected(selected: Boolean, item: UserUpdateAdminDto?) {
				formView.setSelected(item)
			}
		})

		formView.setItemPersistedListener(object : AbstractFormViewV2.ItemPersistenceListener<UserUpdateAdminDto> {
			override fun onItemPersisted(item: UserUpdateAdminDto?) {
				browseView.onItemPersisted(item)
			}

		})

		this.initialize(
			UserUpdateAdminDto::class.java,
			browseView,
			formView
		)
	}

}