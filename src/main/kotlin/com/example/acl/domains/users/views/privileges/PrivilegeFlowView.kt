package com.example.acl.domains.users.views.privileges

import com.example.acl.domains.users.models.dtos.PrivilegeDto
import com.example.acl.domains.users.models.mappers.PrivilegeMapper
import com.example.acl.domains.users.services.PrivilegeService
import com.example.acl.frontend.base.AbstractBrowseView
import com.example.acl.frontend.base.AbstractFilterView
import com.example.acl.frontend.base.AbstractFlowView
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.layouts.MainLayout
import com.example.acl.routing.Route

@com.vaadin.flow.router.Route(Route.V1.VAADIN_WEB_PRIVILEGES, layout = MainLayout::class)
class PrivilegeFlowView constructor(
	privilegeService: PrivilegeService,
	privilegeMapper: PrivilegeMapper,
) : AbstractFlowView<PrivilegeDto>() {

	init {
		val filterView = PrivilegeFilterView()
		val browseView = PrivilegeBrowseView(privilegeService, privilegeMapper)
		val formView = PrivilegeFormView(privilegeService, privilegeMapper)

		filterView.setFilterListener(object : AbstractFilterView.FilterListener {
			override fun onFilterSubmitted(result: Map<String, Any?>) {
				browseView.onFilterSubmitted(result)
			}
		})

		browseView.setItemSelectionListener(object : AbstractBrowseView.ItemSelectionListener<PrivilegeDto> {
			override fun onItemSelected(selected: Boolean, item: PrivilegeDto?) {
				formView.setSelected(item)
			}
		})

		formView.setItemPersistedListener(object : AbstractFormView.ItemPersistenceListener<PrivilegeDto> {
			override fun onItemPersisted(item: PrivilegeDto?) {
				browseView.onItemPersisted(item)
			}
		})

		this.initialize(
			PrivilegeDto::class.java,
			filterView,
			browseView,
			formView
		)
	}
}
