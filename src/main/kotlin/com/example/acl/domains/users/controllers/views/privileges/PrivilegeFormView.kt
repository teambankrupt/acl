package com.example.acl.domains.users.controllers.views.privileges

import com.example.acl.domains.users.models.dtos.PrivilegeDto
import com.example.acl.domains.users.models.mappers.PrivilegeMapper
import com.example.acl.domains.users.services.PrivilegeService
import com.example.acl.frontend.base.AbstractFormView
import com.example.acl.frontend.components.inputs.GroupedInput
import com.example.acl.frontend.components.inputs.TextInput
import com.example.acl.frontend.components.layouts.FormLayout
import com.example.acl.frontend.models.FieldValidator
import com.example.acl.frontend.utils.Notifications
import com.example.auth.entities.Privilege
import com.example.auth.enums.AccessLevels
import com.example.common.utils.ExceptionUtil
import com.example.coreweb.listeners.EndpointsListener
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button
import org.springframework.web.bind.annotation.RequestMethod

class PrivilegeFormView(
	private val privilegeService: PrivilegeService,
	private val privilegeMapper: PrivilegeMapper
) : AbstractFormView<PrivilegeDto>() {

	lateinit var txtLabel: TextInput
	lateinit var txtName: TextInput
	lateinit var txtDescription: TextInput
	lateinit var ckReadAccesses: GroupedInput<String, String>
	lateinit var ckCreateAccesses: GroupedInput<String, String>
	lateinit var ckUpdateAccesses: GroupedInput<String, String>
	lateinit var ckDeleteAccesses: GroupedInput<String, String>

	override fun initForm(formLayout: FormLayout) {
		this.initFields()

		formLayout.addInputs(
			listOf(
				this.txtLabel, this.txtName, this.txtDescription,
				this.ckReadAccesses, this.ckCreateAccesses, this.ckUpdateAccesses, this.ckDeleteAccesses
			)
		)
	}

	private fun initFields() {
		this.txtLabel = TextInput("label", "Label", FieldValidator({ it.length > 3 }, "Must be >=3"))
		this.txtName = TextInput("name", "Name", FieldValidator({ it.length > 3 }, "Must be >=3"))
		this.txtDescription =
			TextInput("description", "Description", FieldValidator({ it != null }, "Must not be null"))

		val readEndPoints = EndpointsListener.getEndpoints(RequestMethod.GET)
		this.ckReadAccesses = GroupedInput<String, String>("urlsReadAccess", "Read Accesses", null)
			.withDataProvider({
				readEndPoints.map { it to it }.stream()
			}, {
				readEndPoints.size
			})
			.setSelectedValues(
				this.getExistingPrivileges(getSelected(), AccessLevels.READ)?.map { it to it } ?: listOf()
			)
		val createEndPoints = EndpointsListener.getEndpoints(RequestMethod.POST)
		this.ckCreateAccesses = GroupedInput<String, String>("urlsCreateAccess", "Create Accesses", null)
			.withDataProvider({
				createEndPoints.map { it to it }.stream()
			}, {
				createEndPoints.size
			})
			.setSelectedValues(
				this.getExistingPrivileges(getSelected(), AccessLevels.CREATE)?.map { it to it } ?: listOf()
			)

		val updateEndpoints = EndpointsListener.getEndpoints(RequestMethod.PATCH)
		updateEndpoints.addAll(EndpointsListener.getEndpoints(RequestMethod.PUT))
		this.ckUpdateAccesses = GroupedInput<String, String>("urlsUpdateAccess", "Update Accesses", null)
			.withDataProvider({
				updateEndpoints.map { it to it }.stream()
			}, {
				updateEndpoints.size
			})
			.setSelectedValues(
				this.getExistingPrivileges(getSelected(), AccessLevels.UPDATE)?.map { it to it } ?: listOf()
			)

		val deleteEndpoints = EndpointsListener.getEndpoints(RequestMethod.DELETE)
		this.ckDeleteAccesses = GroupedInput<String, String>("urlsDeleteAccess", "Delete Accesses", null)
			.withDataProvider({
				deleteEndpoints.map { it to it }.stream()
			}, {
				deleteEndpoints.size
			})
			.setSelectedValues(
				this.getExistingPrivileges(getSelected(), AccessLevels.DELETE)?.map { it to it } ?: listOf()
			)
	}


	override fun onItemSelected(item: PrivilegeDto?) {
		if (item == null) return
		this.txtLabel.value = item.label
		this.txtName.value = item.name
		this.txtDescription.value = item.description ?: ""

		val readPrivs = this.getExistingPrivileges(item, AccessLevels.READ)
		this.ckReadAccesses.setSelectedValues(readPrivs?.map { it to it } ?: listOf())

		val createPrivs = this.getExistingPrivileges(item, AccessLevels.CREATE)
		this.ckCreateAccesses.setSelectedValues(createPrivs?.map { it to it } ?: listOf())

		val updatePrivs = this.getExistingPrivileges(item, AccessLevels.UPDATE)
		this.ckUpdateAccesses.setSelectedValues(updatePrivs?.map { it to it } ?: listOf())

		val deletePrivs = this.getExistingPrivileges(item, AccessLevels.DELETE)
		this.ckDeleteAccesses.setSelectedValues(deletePrivs?.map { it to it } ?: listOf())
	}

	fun getExistingPrivileges(dto: PrivilegeDto?, accessLevel: AccessLevels): List<String>? {
		if (dto == null) return ArrayList()
		val selectedPrivilege = dto.id?.let {
			this.privilegeService.find(it)
				.orElseThrow { ExceptionUtil.notFound(Privilege::class.java, it) }
		}

		return selectedPrivilege?.urlAccesses
			?.filter { it.accessLevel == accessLevel }
			?.map { it.url }
	}

	override fun onSaveAction(event: ClickEvent<Button>, result: Map<String, Any?>) {
		val dto = PrivilegeDto()
		dto.label = result["label"] as String
		dto.name = result["name"] as String
		dto.description = result["description"] as String

		val readUrls = result["urlsReadAccess"] as Set<Pair<String, String>>?
		dto.urlsReadAccess = readUrls?.map { it.second } ?: listOf()

		val createUrls = result["urlsCreateAccess"] as Set<Pair<String, String>>?
		dto.urlsCreateAccess = createUrls?.map { it.second } ?: listOf()

		val updateUrls = result["urlsUpdateAccess"] as Set<Pair<String, String>>?
		dto.urlsUpdateAccess = updateUrls?.map { it.second } ?: listOf()


		val deleteUrls = result["urlsDeleteAccess"] as Set<Pair<String, String>>?
		dto.urlsDeleteAccess = deleteUrls?.map { it.second } ?: listOf()

		val selectedPrivilege = getSelected()?.id?.let { this.privilegeService.find(it).get() }
		this.privilegeService.save(this.privilegeMapper.map(dto, selectedPrivilege))
		Notifications.success("Successfully saved!")
	}

	override fun onEditModeChange(editMode: Boolean) {

	}

	override fun onCancelAction(event: ClickEvent<Button>) {

	}
}
