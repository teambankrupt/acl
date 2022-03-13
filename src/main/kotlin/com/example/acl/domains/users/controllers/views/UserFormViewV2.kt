package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractFormViewV2
import com.example.acl.frontend.components.TextInput
import com.example.acl.frontend.components.layouts.FormLayout
import com.example.cms.domains.fileuploads.services.FileUploadService
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button

class UserFormViewV2(
	private val userService: UserService,
	private val userMapper: UserMapper,
	private val roleService: RoleService,
	private val fileUploadService: FileUploadService
) : AbstractFormViewV2<UserUpdateAdminDto>() {


	override fun onEditModeChange(editMode: Boolean) {
		println(editMode)
	}

	override fun onCancelAction(event: ClickEvent<Button>) {
		println(event)
	}

	override fun initForm(formLayout: FormLayout) {
		formLayout.addInputs(
			listOf(
				TextInput("name", "Name")
			)
		)
	}

	override fun onSaveAction(event: ClickEvent<Button>, result: Map<String, Any?>) {
		result.entries.forEach { println(it) }
	}

}