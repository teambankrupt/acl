package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.components.AbstractInput
import com.example.acl.frontend.components.AutoCompleteTextField
import com.example.acl.frontend.components.GenericValueInput
import com.example.auth.enums.Genders
import com.vaadin.componentfactory.Autocomplete

class UserFormView : AbstractFormView<UserUpdateAdminDto>(UserUpdateAdminDto::class.java) {

	override fun defineFormFields(): Map<String, AbstractInput>? {
		return return mapOf(
			"avatar" to GenericValueInput("avatar", "Avatar"),
			"name" to GenericValueInput("name", "Name"),
			"username" to GenericValueInput("username", "Username"),
			"gender" to AutoCompleteTextField("gender", "Gender", genderAutoComplete()),
			"email" to GenericValueInput("email", "Email"),
			"phone" to GenericValueInput("phone", "Phone")
		)
	}

	private fun genderAutoComplete(): Autocomplete {
		val genderInput = Autocomplete(5)
		genderInput.label = "Find what you want:"
		genderInput.setPlaceholder("search ...")
		genderInput.addChangeListener { event ->
			val text: String = event.value
			genderInput.options = Genders.values().map { it.name }
		}
		return genderInput
	}

}