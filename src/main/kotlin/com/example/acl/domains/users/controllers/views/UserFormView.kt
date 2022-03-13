package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.components.deprecated.*
import com.example.acl.frontend.components.deprecated.AutoCompleteTextField.AcListener
import com.example.acl.frontend.models.FileDefinition
import com.example.acl.frontend.models.FormValidator
import com.example.acl.frontend.utils.Notifications
import com.example.auth.config.security.SecurityContext
import com.example.auth.enums.Genders
import com.example.cms.domains.fileuploads.models.entities.UploadProperties
import com.example.cms.domains.fileuploads.services.FileUploadService
import com.vaadin.componentfactory.Autocomplete
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button

class UserFormView(
	private val userService: UserService,
	private val userMapper: UserMapper,
	private val roleService: RoleService,
	private val fileUploadService: FileUploadService
) : AbstractFormView<UserUpdateAdminDto>(UserUpdateAdminDto::class.java), UploadInput.FileUploadListener {
	private var selectedItem: UserUpdateAdminDto? = null
	private var avatarUrl: String? = null
	private var rolesInput: InputGroup<UserUpdateAdminDto, String, Long>
	private var avatarUpload: UploadInput<UserUpdateAdminDto>

	init {
		this.rolesInput = InputGroup<UserUpdateAdminDto, String, Long>("roleIds", "Roles") { true }.withDataProvider({
			roleService.findAll().map { it.name to it.id }.stream()
		}, { roleService.findAll().count() }).withValueChangeListener { e ->
			if (this.selectedItem == null) this.selectedItem = UserUpdateAdminDto()
			this.selectedItem!!.roleIds = e.value.map { it.second }
		}.setDefaultValues(
			getDefaultRoles()
		)

		this.avatarUpload = UploadInput<UserUpdateAdminDto>(
			"avatar",
			"Avatar",
			{ !(it as String?).isNullOrBlank() },
			this.fileUploadService,
			FileDefinition("png", "uploads", SecurityContext.getLoggedInUsername()),
			this,
			false
		)

		this.initForm(mutableMapOf())
	}

	override fun defineFormFields(): Map<String, AbstractInput<UserUpdateAdminDto>>? {
		return mapOf(
//			"avatar" to GenericValueInput<UserUpdateAdminDto>("avatar", "Avatar") {
//				!it.avatar.isNullOrBlank()
//			},
			"avatar" to this.avatarUpload,
			"name" to GenericValueInput<UserUpdateAdminDto>("name", "Name", null),
			"username" to GenericValueInput<UserUpdateAdminDto>("username", "Username", null),
			"password" to GenericValueInput<UserUpdateAdminDto>("password", "Password", null),
			"gender" to GenericValueInput<UserUpdateAdminDto>("gender", "Gender", null),
			"email" to GenericValueInput<UserUpdateAdminDto>("email", "Email", null),
			"phone" to GenericValueInput<UserUpdateAdminDto>("phone", "Phone", null),
			"enabled" to GenericValueInput<UserUpdateAdminDto>("enabled", "Enabled", null),
			"accountNonLocked" to GenericValueInput<UserUpdateAdminDto>(
				"accountNonLocked",
				"Account Non Locked",
				null
			),
			"accountNonExpired" to GenericValueInput<UserUpdateAdminDto>(
				"accountNonExpired", "Account Non Expired", null
			),
			"credentialsNonExpired" to GenericValueInput<UserUpdateAdminDto>(
				"credentialsNonExpired", "Credentials Non Expired", null
			),
			"roleIds" to this.rolesInput
		)
	}

	private fun getDefaultRoles(): List<Pair<String, Long>> {
		val user = this.selectedItem ?: return mutableListOf()
		val roles = roleService.findByIds(user.roleIds)
		return roles.map { it.name to it.id }
	}

	override fun getDefaultSingleSelectionValues(): Map<String, String> {
		val item = this.selectedItem

		return if (item == null) mapOf() else mapOf(
			"gender" to item.gender.name
		)
	}

	private fun getGenderInput(): AutoCompleteTextField<UserUpdateAdminDto> {
		val genderInput = AutoCompleteTextField<UserUpdateAdminDto>("gender", "Gender", null)
		val listener = object : AcListener {
			override fun onAcChange(event: Autocomplete.AucompleteChangeEvent) {
				val users = userService.search(event.value, 0, 5)
				val items = users.content.map { it.name }
				genderInput.setOptions(items)
				items.forEach { print(it) }
				println()
			}

			override fun onAcApplied(event: Autocomplete.AutocompleteValueAppliedEvent) {
				println(event.value)
			}

			override fun onAcCleared(event: Autocomplete.ValueClearEvent) {
				println(event.toString())
			}
		}
		genderInput.setListener(listener)
		return genderInput
	}

	override fun onItemSelected(selected: Boolean, item: UserUpdateAdminDto?) {
		this.selectedItem = item
		this.rolesInput.setDefaultValues(this.getDefaultRoles())
		item?.password = ""
		this.getBinder().readBean(item)
		this.setSelected(true)
	}

	override fun onSaveAction(event: ClickEvent<Button>, dropdownValues: MutableMap<String, String>) {
		val dto = this.selectedItem ?: UserUpdateAdminDto()

		getBinder().writeBean(dto)

		// Prepare dto
		val genderStr = dropdownValues["gender"] ?: Genders.NOT_SPECIFIED.name
		dto.gender = Genders.valueOf(genderStr)
		dto.avatar = this.avatarUrl

		// save entity
		val exUser = this.selectedItem?.id?.let { this.userService.find(it).orElse(null) }
		val user = this.userService.save(this.userMapper.map(dto, exUser))

		// update ui
		this.selectedItem = this.userMapper.mapToAdminDto(user)
		this.getBinder().writeBean(this.selectedItem)

		this.itemPersistenceListener?.onItemPersisted(this.selectedItem)
		Notifications.success("Successfully saved ${user.name} !")

		// avatar upload state
		this.avatarUpload.setEnabled(this.isEditMode())
	}

	override fun onCancelAction(event: ClickEvent<Button>) {
		println(event.toString())
	}

	override fun getBean(): UserUpdateAdminDto {
		return this.selectedItem ?: UserUpdateAdminDto()
	}

	override fun onFileUploaded(properties: UploadProperties, contentLength: Long, mimeType: String) {
		this.avatarUrl = properties.fileUrl
		Notifications.success("File ${properties.fileName} is uploaded")
	}

	override fun onEditModeChange(editMode: Boolean) {
		this.avatarUpload.setEnabled(editMode)
	}

	override fun getFormValidator(): FormValidator<UserUpdateAdminDto> {
		return FormValidator(
			{ true },
			mapOf()
		)
	}


}