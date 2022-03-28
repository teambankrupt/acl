package com.example.acl.domains.users.controllers.views.users

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
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

class UserFormView(
	private val userService: UserService,
	private val userMapper: UserMapper,
	private val roleService: RoleService,
	private val fileUploadService: FileUploadService
) : AbstractFormView<UserUpdateAdminDto>() {
	lateinit var txtName: TextInput
	lateinit var txtUsername: TextInput
	lateinit var txtPhone: TextInput
	lateinit var txtEmail: EmailInput
	lateinit var txtPassword: PasswordInput
	lateinit var cBoxGender: SelectInput<String>
	lateinit var ckRoles: GroupedInput<String, Long>
	lateinit var avatarUpload: SingleUploadInput
	lateinit var ckEnabled: CheckboxInput
	lateinit var ckNonExpired: CheckboxInput
	lateinit var ckNonLocked: CheckboxInput
	lateinit var ckCredentialsNonExpired: CheckboxInput

	override fun onEditModeChange(editMode: Boolean) {
		println("Edit Mode: $editMode")
	}

	override fun onCancelAction(event: ClickEvent<Button>) {
		println("Cancel Action: $event")
	}


	override fun initForm(formLayout: FormLayout) {
		this.initFields()

		formLayout.addInputs(
			listOf(
				this.avatarUpload,
				this.txtName,
				this.txtUsername,
				this.txtPhone,
				this.txtEmail,
				this.txtPassword,
				this.cBoxGender,
				this.ckRoles,
				this.ckEnabled,
				this.ckNonExpired,
				this.ckNonLocked,
				this.ckCredentialsNonExpired
			)
		)
	}

	private fun initFields() {
		this.txtName =
			TextInput("name", "Name", FieldValidator({ it.length >= 3 }, "Name must be at least 3 characters!"))
		this.txtUsername = TextInput(
			"username",
			"Username",
			FieldValidator({ it.length >= 6 }, "Username must be at least 6 characters!")
		)
		this.txtPhone = TextInput("phone", "Phone", null)
		this.txtEmail = EmailInput("email", "Email", null)
		this.txtPassword = PasswordInput("password", "Password", null)
		this.cBoxGender = SelectInput<String>(
			"gender",
			"Gender",
			Genders.values().map { it.name },
			FieldValidator({ it != null }, "Must select a gender")
		)
		this.ckRoles = GroupedInput<String, Long>("roleIds", "Roles", null)
			.withDataProvider({
				roleService.findAll().map { it.name to it.id }.stream()
			}, {
				roleService.findAll().count()
			})
			.setSelectedValues(
				getSelectedRoles(this.getSelected())
			)
		this.avatarUpload = SingleUploadInput(
			"avatar",
			"Avatar",
			this.fileUploadService,
			FileDefinition("png", "uploads", SecurityContext.getLoggedInUsername()),
			null,
			null
		)
		this.ckEnabled = CheckboxInput("enabled", "Enabled", FieldValidator({ true }, ""))
		this.ckNonExpired = CheckboxInput("accountNonExpired", "Account Non Expired", FieldValidator({ true }, ""))
		this.ckNonLocked = CheckboxInput("accountNonLocked", "Account Unlocked", FieldValidator({ true }, ""))
		this.ckCredentialsNonExpired =
			CheckboxInput("credentialsNonExpired", "Credentials Non Expired", FieldValidator({ true }, ""))
	}

	private fun getSelectedRoles(selectedUser: UserUpdateAdminDto?): List<Pair<String, Long>> {
		val user = selectedUser ?: return listOf()
		val roles = roleService.findByIds(user.roleIds)
		return roles.map { it.name to it.id }
	}

	override fun onSaveAction(event: ClickEvent<Button>, result: Map<String, Any?>) {
//		result.entries.forEach { println(it) }
		val dto = this.getSelected() ?: UserUpdateAdminDto()
		dto.avatar = result["avatar"] as String?
		dto.name = result["name"] as String
//		dto.username = result["username"] as String
		val gender = result["gender"] as String
		dto.gender = Genders.valueOf(gender)
		dto.phone = result["phone"] as String
		dto.email = result["email"] as String?
		val roles = result["roleIds"] as Set<Pair<String, Long>>
		dto.roleIds = roles.map { it.second }
		dto.enabled = result["enabled"] as Boolean
		dto.accountNonExpired = result["accountNonExpired"] as Boolean
		dto.accountNonLocked = result["accountNonLocked"] as Boolean
		dto.credentialsNonExpired = result["credentialsNonExpired"] as Boolean

		val exUser = this.getSelected()?.id?.let { this.userService.find(it).orElse(null) }
		val user = this.userService.save(this.userMapper.map(dto, exUser))

		this.itemPersistenceListener?.onItemPersisted(this.userMapper.mapToAdminDto(user))
	}

	override fun onItemSelected(item: UserUpdateAdminDto?) {
		this.avatarUpload.setDefaultImage(item?.avatar, "Avatar")
		this.txtName.value = item?.name
		this.txtUsername.value = item?.username
		this.txtPhone.value = item?.phone
		this.txtEmail.value = item?.email
		this.txtPassword.value = item?.password
		this.cBoxGender.value = item?.gender?.name
		this.ckRoles.setSelectedValues(this.getSelectedRoles(item))
		this.ckEnabled.value = item?.enabled
		this.ckNonExpired.value = item?.accountNonExpired
		this.ckNonLocked.value = item?.accountNonLocked
		this.ckCredentialsNonExpired.value = item?.credentialsNonExpired
	}

}