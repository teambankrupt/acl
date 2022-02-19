package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.components.AbstractInput
import com.example.acl.frontend.components.AutoCompleteTextField
import com.example.acl.frontend.components.AutoCompleteTextField.AcListener
import com.example.acl.frontend.components.GenericValueInput
import com.vaadin.componentfactory.Autocomplete
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button

class UserFormView(
	private val userService: UserService
) : AbstractFormView<UserUpdateAdminDto>(UserUpdateAdminDto::class.java) {
	private var selectedItem: UserUpdateAdminDto? = null

	override fun defineFormFields(): Map<String, AbstractInput>? {
		return mapOf(
			"avatar" to GenericValueInput("avatar", "Avatar"),
			"name" to GenericValueInput("name", "Name"),
			"username" to GenericValueInput("username", "Username"),
			"gender" to GenericValueInput("gender", "Gender"),
			"email" to GenericValueInput("email", "Email"),
			"phone" to GenericValueInput("phone", "Phone"),
			"enabled" to GenericValueInput("enabled", "Enabled"),
			"accountNonLocked" to GenericValueInput("accountNonLocked", "Account Non Locked"),
			"accountNonExpired" to GenericValueInput("accountNonExpired", "Account Non Expired"),
			"credentialsNonExpired" to GenericValueInput("credentialsNonExpired", "Credentials Non Expired")
		)
	}

	override fun getDefaultSelectValues(): Map<String, String> {
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
		this.getBinder(selected).readBean(item)
	}

	override fun onSaveAction(event: ClickEvent<Button>, dropdownValues: MutableMap<String, String>) {
		if (this.selectedItem == null) this.selectedItem = UserUpdateAdminDto()
		getBinder(true).writeBean(this.selectedItem)
		println(this.selectedItem)
	}

	override fun onCancelAction(event: ClickEvent<Button>) {
		println(event.toString())
	}


}