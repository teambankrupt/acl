package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.components.AbstractInput
import com.example.acl.frontend.components.AutoCompleteTextField
import com.example.acl.frontend.components.AutoCompleteTextField.AcListener
import com.example.acl.frontend.components.GenericValueInput
import com.vaadin.componentfactory.Autocomplete

class UserFormView(
	private val userService: UserService
) : AbstractFormView<UserUpdateAdminDto>(UserUpdateAdminDto::class.java) {

	override fun defineFormFields(): Map<String, AbstractInput>? {
		return mapOf(
			"avatar" to GenericValueInput("avatar", "Avatar"),
			"name" to GenericValueInput("name", "Name"),
			"username" to GenericValueInput("username", "Username"),
			"gender" to this.getGenderInput() ,
			"email" to GenericValueInput("email", "Email"),
			"phone" to GenericValueInput("phone", "Phone")
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


}