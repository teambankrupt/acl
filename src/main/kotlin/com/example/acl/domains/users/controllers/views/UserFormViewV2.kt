package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractFormViewV2
import com.example.acl.frontend.components.ComboBoxInput
import com.example.acl.frontend.components.TextInput
import com.example.acl.frontend.components.layouts.FormLayout
import com.example.auth.enums.Genders
import com.example.cms.domains.fileuploads.services.FileUploadService
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button

class UserFormViewV2(
	private val userService: UserService,
	private val userMapper: UserMapper,
	private val roleService: RoleService,
	private val fileUploadService: FileUploadService
) : AbstractFormViewV2<UserUpdateAdminDto>() {
	lateinit var txtName: TextInput
	lateinit var cBoxGender: ComboBoxInput<String>

	override fun onEditModeChange(editMode: Boolean) {
		println(editMode)
	}

	override fun onCancelAction(event: ClickEvent<Button>) {
		println(event)
	}


	override fun initForm(formLayout: FormLayout) {
		this.txtName = TextInput("name", "Name")
		this.cBoxGender = ComboBoxInput<String>("gender", "Gender").withItems(
			Genders.values().map { it.name })

		formLayout.addInputs(
			listOf(
				txtName,
				cBoxGender
			)
		)
	}

	override fun onSaveAction(event: ClickEvent<Button>, result: Map<String, Any?>) {
		result.entries.forEach { println(it) }
	}

	override fun onItemSelected(item: UserUpdateAdminDto?) {
		this.txtName.value = item?.name
		this.cBoxGender.value = item?.gender?.name
	}

}