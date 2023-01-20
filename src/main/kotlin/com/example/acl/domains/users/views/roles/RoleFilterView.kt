package com.example.acl.domains.users.views.roles

import com.example.acl.domains.users.models.dtos.RoleDto
import com.example.acl.frontend.base.AbstractFilterView
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.components.inputs.CheckboxInput
import com.example.acl.frontend.components.inputs.TextInput
import com.example.acl.frontend.components.layouts.FormLayout
import com.example.acl.frontend.models.FieldValidator
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button

class RoleFilterView : AbstractFilterView<RoleDto>() {
	lateinit var txtSearch: TextInput
	lateinit var ckRestricted: CheckboxInput

	override fun initForm(formLayout: FormLayout) {
		this.txtSearch = TextInput("query", "Search", null)
		this.ckRestricted = CheckboxInput("restricted", "Restricted", null)

		formLayout.addInputs(
			listOf(
				this.txtSearch,
				this.ckRestricted
			)
		)
	}


}
