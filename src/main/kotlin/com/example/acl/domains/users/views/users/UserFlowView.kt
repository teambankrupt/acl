package com.example.acl.domains.users.views.users

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractBrowseView
import com.example.acl.frontend.base.AbstractFilterView
import com.example.acl.frontend.base.AbstractFlowView
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.layouts.MainLayout
import com.example.acl.routing.Route
import com.example.filehandler.domains.fileuploads.services.FileUploadService

@com.vaadin.flow.router.Route(Route.V1.VAADIN_WEB_USERS, layout = MainLayout::class)
class UserFlowView(
	userService: UserService,
	userMapper: UserMapper,
	roleService: RoleService,
	uploadService: FileUploadService
) : AbstractFlowView<UserUpdateAdminDto>() {

	init {
		val filterView = UserFilterView(roleService)
		val browseView = UserBrowseView(userService, userMapper)
		val formView = UserFormView(userService, userMapper, roleService, uploadService)

		filterView.setFilterListener(object : AbstractFilterView.FilterListener {
			override fun onFilterSubmitted(result: Map<String, Any?>) {
				browseView.onFilterSubmitted(result)
			}
		})

		browseView.setItemSelectionListener(object : AbstractBrowseView.ItemSelectionListener<UserUpdateAdminDto> {
			override fun onItemSelected(selected: Boolean, item: UserUpdateAdminDto?) {
				formView.setSelected(item)
			}
		})

		formView.setItemPersistedListener(object : AbstractFormView.ItemPersistenceListener<UserUpdateAdminDto> {
			override fun onItemPersisted(item: UserUpdateAdminDto?) {
				browseView.onItemPersisted(item)
			}

		})

		this.initialize(
			UserUpdateAdminDto::class.java,
			filterView,
			browseView,
			formView
		)
	}

}
