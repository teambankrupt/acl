package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractFormViewV2
import com.example.acl.frontend.components.*
import com.example.acl.frontend.components.layouts.FormLayout
import com.example.acl.frontend.models.FieldValidator
import com.example.acl.frontend.models.FileDefinition
import com.example.auth.config.security.SecurityContext
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
	lateinit var txtUsername: TextInput
	lateinit var txtPhone: TextInput
	lateinit var txtEmail: EmailInput
	lateinit var txtPassword: PasswordInput
	lateinit var cBoxGender: SelectInput<String>
	lateinit var ckRoles: GroupedInput<String, Long>
	lateinit var avatarUpload: MultiUploadInput

	override fun onEditModeChange(editMode: Boolean) {
		println("Edit Mode: $editMode")
	}

	override fun onCancelAction(event: ClickEvent<Button>) {
		println("Cancel Action: $event")
	}


	override fun initForm(formLayout: FormLayout) {
		this.txtName =
			TextInput("name", "Name", FieldValidator({ it.length >= 3 }, "Name must be at least 3 characters!"))
		this.txtUsername = TextInput("username", "Username", null)
		this.txtPhone = TextInput("phone", "Phone", null)
		this.txtEmail = EmailInput("email", "Email", null)
		this.txtPassword = PasswordInput("password", "Password", null)
		this.cBoxGender = SelectInput<String>("gender", "Gender", Genders.values().map { it.name }, null)
		this.ckRoles = GroupedInput<String, Long>("roleIds", "Roles", null)
			.withDataProvider({
				roleService.findAll().map { it.name to it.id }.stream()
			}, {
				roleService.findAll().count()
			})
			.setSelectedValues(
				getSelectedRoles(this.getSelected())
			)
		this.avatarUpload = MultiUploadInput(
			"avatar",
			"Avatar",
			this.fileUploadService,
			FileDefinition("png", "uploads", SecurityContext.getLoggedInUsername()),
			null,
			null
		)

		formLayout.addInputs(
			listOf(
				this.avatarUpload,
				this.txtName,
				this.txtUsername,
				this.txtPhone,
				this.txtEmail,
				this.txtPassword,
				this.cBoxGender,
				this.ckRoles
			)
		)
	}

	private fun getSelectedRoles(selectedUser: UserUpdateAdminDto?): List<Pair<String, Long>> {
		val user = selectedUser ?: return listOf()
		val roles = roleService.findByIds(user.roleIds)
		return roles.map { it.name to it.id }
	}

	override fun onSaveAction(event: ClickEvent<Button>, result: Map<String, Any?>) {
		result.entries.forEach { println(it) }
	}

	override fun onItemSelected(item: UserUpdateAdminDto?) {
		this.txtName.value = item?.name
		this.txtUsername.value = item?.username
		this.txtPhone.value = item?.phone
		this.txtEmail.value = item?.email
		this.txtPassword.value = item?.password
		this.cBoxGender.value = item?.gender?.name
		this.ckRoles.setSelectedValues(this.getSelectedRoles(item))
	}

}