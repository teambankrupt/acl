package com.example.acl.domains.users.controllers.views.roles

import com.example.acl.domains.users.models.dtos.RoleDto
import com.example.acl.domains.users.models.mappers.RoleMapper
import com.example.acl.domains.users.services.PrivilegeService
import com.example.acl.domains.users.services.RoleService
import com.example.acl.frontend.base.AbstractBrowseView
import com.example.acl.frontend.base.AbstractFilterView
import com.example.acl.frontend.base.AbstractFlowView
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.layouts.MainLayout
import com.example.acl.routing.Route

@com.vaadin.flow.router.Route(Route.V1.VAADIN_WEB_ROLES, layout = MainLayout::class)
class RoleFlowView constructor(
	roleService: RoleService,
	roleMapper: RoleMapper,
	privilegeService: PrivilegeService
) : AbstractFlowView<RoleDto>() {

	init {
		val filterView = RoleFilterView()
		val browseView = RoleBrowseView(roleService, roleMapper)
		val formView = RoleFormView(roleService, privilegeService, roleMapper)

		filterView.setFilterListener(object : AbstractFilterView.FilterListener {
			override fun onFilterSubmitted(result: Map<String, Any?>) {
				browseView.onFilterSubmitted(result)
			}
		})

		browseView.setItemSelectionListener(object : AbstractBrowseView.ItemSelectionListener<RoleDto> {
			override fun onItemSelected(selected: Boolean, item: RoleDto?) {
				formView.setSelected(item)
			}
		})

		formView.setItemPersistedListener(object : AbstractFormView.ItemPersistenceListener<RoleDto> {
			override fun onItemPersisted(item: RoleDto?) {
				browseView.onItemPersisted(item)
			}
		})

		this.initialize(
			RoleDto::class.java,
			filterView,
			browseView,
			formView
		)
	}
}
