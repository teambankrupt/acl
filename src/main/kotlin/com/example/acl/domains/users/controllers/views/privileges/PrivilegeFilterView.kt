package com.example.acl.domains.users.controllers.views.privileges

import com.example.acl.domains.users.models.dtos.PrivilegeDto
import com.example.acl.frontend.base.AbstractFilterView
import com.example.acl.frontend.components.inputs.TextInput
import com.example.acl.frontend.components.layouts.FormLayout

class PrivilegeFilterView : AbstractFilterView<PrivilegeDto>() {
	lateinit var txtSearch: TextInput

	override fun initForm(formLayout: FormLayout) {
		this.txtSearch = TextInput("query", "Search", null)

		formLayout.addInputs(
			listOf(
				this.txtSearch,
			)
		)
	}


}
