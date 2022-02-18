package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.frontend.base.AbstractFormView

class UserFormView : AbstractFormView<UserUpdateAdminDto>(UserUpdateAdminDto::class.java) {

	override fun defineFormFields(): Map<String, String>? {
		return null
	}

}