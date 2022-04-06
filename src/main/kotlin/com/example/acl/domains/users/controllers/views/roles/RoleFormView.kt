package com.example.acl.domains.users.controllers.views.roles

import com.example.acl.domains.users.models.dtos.RoleDto
import com.example.acl.domains.users.models.mappers.RoleMapper
import com.example.acl.domains.users.services.PrivilegeService
import com.example.acl.domains.users.services.RoleService
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.components.inputs.CheckboxInput
import com.example.acl.frontend.components.inputs.GroupedInput
import com.example.acl.frontend.components.inputs.TextInput
import com.example.acl.frontend.components.layouts.FormLayout
import com.example.acl.frontend.models.FieldValidator
import com.example.acl.frontend.utils.Notifications
import com.example.auth.enums.Privileges
import com.example.common.utils.ExceptionUtil
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button

class RoleFormView(
	private val roleService: RoleService,
	private val privilegeService: PrivilegeService,
	private val roleMapper: RoleMapper
) : AbstractFormView<RoleDto>() {

	lateinit var nameView: TextInput
	lateinit var ckRestricted: CheckboxInput
	lateinit var descriptionView: TextInput
	lateinit var ckPrivileges: GroupedInput<String, Long>

	override fun initForm(formLayout: FormLayout) {
		this.nameView = TextInput("name", "Name", FieldValidator({ it.length > 3 }, "Must be >=3"))
		this.ckRestricted = CheckboxInput("restricted", "Restricted", null)
		this.descriptionView =
			TextInput("description", "Description", FieldValidator({ it != null }, "Must not be null"))
		this.ckPrivileges = GroupedInput<String, Long>("privilegeIds", "Privileges", null)
			.withDataProvider({
				privilegeService.findAll().map { it.name to it.id }.stream()
			}, {
				privilegeService.findAll().count()
			})
			.setSelectedValues(
				getSelectedPrivileges(this.getSelected())
			)

		formLayout.addInputs(
			listOf(
				this.nameView, this.ckRestricted, this.descriptionView, this.ckPrivileges
			)
		)
	}

	private fun getSelectedPrivileges(selected: RoleDto?): Iterable<Pair<String, Long>> {
		val role = selected ?: return listOf()
		val ps = role.privileges ?: return listOf()
		val privileges = privilegeService.findByIds(ps.map { it.id ?: 0 })
		return privileges.map { it.name to it.id }
	}

	override fun onItemSelected(item: RoleDto?) {
		if (item == null) return
		this.nameView.value = item.name
		this.ckRestricted.value = item.restricted
		this.descriptionView.value = item.description ?: ""
		this.ckPrivileges.setSelectedValues(this.getSelectedPrivileges(item))
	}

	override fun onSaveAction(event: ClickEvent<Button>, result: Map<String, Any?>) {
		val dto = RoleDto()
		dto.name = result["name"] as String
		dto.restricted = result["restricted"] as Boolean
		dto.description = result["description"] as String
		val privileges = result["privilegeIds"] as Set<Pair<String, Long>>
		dto.privilegeIds = privileges.map { it.second }

		val selectedRole = getSelected()?.id?.let { this.roleService.find(it).get() }

		// Validation so that admin can't be updated
		if (privileges.map { it.first.uppercase() }.contains(Privileges.ADMINISTRATION.name)
			|| (selectedRole != null && selectedRole.isAdmin)
		) {
			Notifications.error("Admin can't be modified!")
			return
		}

		this.roleService.save(this.roleMapper.map(dto, selectedRole))
		Notifications.success("Successfully saved!")
	}

	override fun onEditModeChange(editMode: Boolean) {

	}

	override fun onCancelAction(event: ClickEvent<Button>) {

	}
}
