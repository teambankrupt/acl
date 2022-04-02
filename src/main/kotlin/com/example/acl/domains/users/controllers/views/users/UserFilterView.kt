package com.example.acl.domains.users.controllers.views.users

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractFilterView
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.components.inputs.*
import com.example.acl.frontend.components.layouts.FormLayout
import com.example.acl.frontend.models.FieldValidator
import com.example.acl.frontend.models.FileDefinition
import com.example.auth.config.security.SecurityContext
import com.example.auth.enums.Genders
import com.example.filehandler.domains.fileuploads.services.FileUploadService
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button

class UserFilterView(
	private val roleService: RoleService
) : AbstractFilterView<UserUpdateAdminDto>() {
	lateinit var cbxRole: SelectInput<String>

	override fun initForm(formLayout: FormLayout) {
		this.initFields()

		formLayout.addInputs(
			listOf(
				this.cbxRole
			)
		)
	}

	private fun initFields() {
		this.cbxRole = SelectInput(
			"role",
			"Select Role",
			this.roleService.findAll().map { it.name },
			null
		)
	}

}
