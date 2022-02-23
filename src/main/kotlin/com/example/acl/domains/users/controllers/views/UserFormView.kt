package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.components.*
import com.example.acl.frontend.components.AutoCompleteTextField.AcListener
import com.example.acl.frontend.utils.Notifications
import com.example.auth.enums.Genders
import com.vaadin.componentfactory.Autocomplete
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.data.provider.CallbackDataProvider
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.ListDataProvider

class UserFormView(
	private val userService: UserService,
	private val userMapper: UserMapper,
	private val roleService: RoleService
) : AbstractFormView<UserUpdateAdminDto>(UserUpdateAdminDto::class.java) {
	private var selectedItem: UserUpdateAdminDto? = null
	private var rolesInput: InputGroup<String, Long>

	init {
		this.rolesInput = InputGroup<String, Long>("roleIds", "Roles")
			.withDataProvider(
				{
					roleService.findAll().map { it.name to it.id }.stream()
				},
				{ roleService.findAll().count() }
			).withValueChangeListener { e ->
				if (this.selectedItem == null) this.selectedItem = UserUpdateAdminDto()
				this.selectedItem!!.roleIds = e.value.map { it.second }
			}.setDefaultValues(
				getDefaultRoles()
			)


		this.initForm(mutableMapOf())
	}

	override fun defineFormFields(): Map<String, AbstractInput>? {
		return mapOf(
			"avatar" to GenericValueInput("avatar", "Avatar"),
			"name" to GenericValueInput("name", "Name"),
			"username" to GenericValueInput("username", "Username"),
			"password" to GenericValueInput("password", "Password"),
			"gender" to GenericValueInput("gender", "Gender"),
			"email" to GenericValueInput("email", "Email"),
			"phone" to GenericValueInput("phone", "Phone"),
			"enabled" to GenericValueInput("enabled", "Enabled"),
			"accountNonLocked" to GenericValueInput("accountNonLocked", "Account Non Locked"),
			"accountNonExpired" to GenericValueInput("accountNonExpired", "Account Non Expired"),
			"credentialsNonExpired" to GenericValueInput("credentialsNonExpired", "Credentials Non Expired"),
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

	private fun getGenderInput(): AutoCompleteTextField {
		val genderInput = AutoCompleteTextField("gender", "Gender")
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

		val genderStr = dropdownValues["gender"] ?: Genders.NOT_SPECIFIED.name
		dto.gender = Genders.valueOf(genderStr)
		val exUser = this.selectedItem?.id?.let { this.userService.find(it).orElse(null) }
		val user = this.userService.save(this.userMapper.map(dto, exUser))
		this.selectedItem = this.userMapper.mapToAdminDto(user)

		this.getBinder().writeBean(this.selectedItem)

		this.itemPersistenceListener?.onItemPersisted(this.selectedItem)
		Notifications.success("Successfully saved ${user.name} !")
	}

	override fun onCancelAction(event: ClickEvent<Button>) {
		println(event.toString())
	}


}