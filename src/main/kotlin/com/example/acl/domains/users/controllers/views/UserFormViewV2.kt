package com.example.acl.domains.users.controllers.views

import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.mappers.UserMapper
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
import com.example.acl.frontend.base.AbstractFormViewV2
import com.example.acl.frontend.components.GroupedInput
import com.example.acl.frontend.components.SelectInput
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
	lateinit var cBoxGender: SelectInput<String>
	lateinit var ckRoles: GroupedInput<String, Long>

	override fun onEditModeChange(editMode: Boolean) {
		println(editMode)
	}

	override fun onCancelAction(event: ClickEvent<Button>) {
		println(event)
	}


	override fun initForm(formLayout: FormLayout) {
		this.txtName = TextInput("name", "Name")
		this.cBoxGender = SelectInput<String>("gender", "Gender", Genders.values().map { it.name })
		this.ckRoles = GroupedInput<String, Long>("roleIds", "Roles")
			.withDataProvider({
				roleService.findAll().map { it.name to it.id }.stream()
			}, {
				roleService.findAll().count()
			})
//			.withValueChangeListener { e ->
//				if (this.selectedItem == null) this.selectedItem = UserUpdateAdminDto()
//				this.selectedItem!!.roleIds = e.value.map { it.second }
//			}
			.setDefaultValues(
				getDefaultRoles()
			)
//			.withItems(Genders.values().map { it.name })

		formLayout.addInputs(
			listOf(
				txtName,
				cBoxGender,
				ckRoles
			)
		)
	}

	private fun getDefaultRoles(): List<Pair<String, Long>> {
		val user = this.getSelected() ?: return mutableListOf()
		val roles = roleService.findByIds(user.roleIds)
		return roles.map { it.name to it.id }
	}

	override fun onSaveAction(event: ClickEvent<Button>, result: Map<String, Any?>) {
		result.entries.forEach { println(it) }
	}

	override fun onItemSelected(item: UserUpdateAdminDto?) {
		this.txtName.value = item?.name
		this.cBoxGender.value = item?.gender?.name
	}

}