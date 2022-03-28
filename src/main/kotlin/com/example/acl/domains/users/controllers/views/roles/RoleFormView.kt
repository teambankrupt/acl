package com.example.acl.domains.users.controllers.views.roles

import com.example.acl.domains.users.models.dtos.RoleDto
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.components.inputs.TextInput
import com.example.acl.frontend.components.layouts.FormLayout
import com.example.acl.frontend.models.FieldValidator
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button

class RoleFormView : AbstractFormView<RoleDto>() {
	lateinit var nameView: TextInput
	lateinit var descriptionView: TextInput

	override fun initForm(formLayout: FormLayout) {
		this.nameView = TextInput("name", "Name", FieldValidator({ it.length > 5 }, "Must be >5"))
		this.descriptionView = TextInput("description", "Description", FieldValidator({ it != null }, "Must not null"))

		formLayout.addInputs(
			listOf(
				this.nameView, this.descriptionView
			)
		)
	}

	override fun onItemSelected(item: RoleDto?) {
		if (item == null) return
		this.nameView.value = item.name
		this.descriptionView.value = item.description ?: ""
	}

	override fun onSaveAction(event: ClickEvent<Button>, result: Map<String, Any?>) {
		println(result)
	}

	override fun onEditModeChange(editMode: Boolean) {

	}

	override fun onCancelAction(event: ClickEvent<Button>) {

	}
}