package com.example.acl.frontend.base

import com.example.acl.frontend.components.layouts.FormLayout
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import java.util.*

abstract class AbstractFormViewV2<T> : Div() {
	private var editMode: Boolean = false
	private var formLayout: FormLayout = FormLayout()
	private var buttonLayout: HorizontalLayout
	lateinit var btnSave: Button
	lateinit var btnCancel: Button
	private var selectedItem: T? = null

	var itemPersistenceListener: ItemPersistenceListener<T>? = null

	init {
		this.className = "flex flex-col space-s"
		this.width = "400px"
		val editorDiv = Div()
		editorDiv.className = "p-l flex-grow"
		this.add(editorDiv)

		this.buttonLayout = this.createButtonLayout()

		this.add(this.formLayout, this.buttonLayout)
	}

	override fun onAttach(attachEvent: AttachEvent?) {
		super.onAttach(attachEvent)

		this.initForm(this.formLayout)
		this.formLayout.initialize(this.editMode)
	}

	abstract fun initForm(formLayout: FormLayout)

	fun setSelected(item: T?) {
		this.selectedItem = item
		this.resolveBtnState(this.btnSave, this.btnCancel)
		this.onItemSelected(item)
	}

	abstract fun onItemSelected(item: T?)

	fun getSelected(): T? {
		return this.selectedItem
	}

	fun hasSelectedItem(): Boolean {
		return this.selectedItem != null
	}

	private fun createButtonLayout(): HorizontalLayout {
		val buttonLayout = HorizontalLayout()
		buttonLayout.className = "w-full flex-wrap bg-contrast-5 py-s px-l"
		buttonLayout.isSpacing = true

		this.btnCancel = Button("Reset")
		this.btnCancel.setId("id_cancel")
		this.btnCancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
		this.btnCancel.addClickListener {
			if (this.isFormResettable()) {
				this.formLayout.resetValues()
			} else {
				this.onButtonClicked(btnCancel.id, it)
			}
			this.resolveBtnState(btnCancel)
		}

		this.btnSave = Button("Save")
		this.btnSave.setId("id_save")
		this.btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
		this.btnSave.addClickListener {
			this.onButtonClicked(this.btnSave.id, it)
		}

		this.resolveBtnState(this.btnSave, this.btnCancel)
		buttonLayout.add(this.btnSave, this.btnCancel)
		return buttonLayout
	}


	fun isFormResettable(): Boolean {
		return !this.editMode
	}

	private fun onButtonClicked(btnId: Optional<String>, event: ClickEvent<Button>) {
		if (!btnId.isPresent) return

		if (this.editMode) {
			if (btnId.get() == "id_save") {
				this.onSaveAction(event, this.formLayout.getValues())
			} else if (btnId.get() == "id_cancel") {
				this.onCancelAction(event)
			}
		}

		this.setEditMode(!this.editMode)
		this.resolveBtnState(this.btnSave, this.btnCancel)
	}

	fun setEditMode(editMode: Boolean) {
		this.editMode = editMode
		this.formLayout.initialize(editMode)
		this.onEditModeChange(editMode)
	}

	abstract fun onSaveAction(event: ClickEvent<Button>, result: Map<String, Any?>)

	abstract fun onEditModeChange(editMode: Boolean)

	abstract fun onCancelAction(event: ClickEvent<Button>)

	private fun resolveBtnState(vararg buttons: Button) {
		buttons.forEach {

			if (it.id.get() == "id_save") {
				it.text = if (this.editMode) "Save" else {
					if (this.hasSelectedItem()) "Update" else "Create New"
				}
			} else if (it.id.get() == "id_cancel") {
				it.text = if (this.isFormResettable())
					"Reset" else "Cancel"
			}

		}
	}


	fun setItemPersistedListener(listener: ItemPersistenceListener<T>) {
		this.itemPersistenceListener = listener
	}

	interface ItemPersistenceListener<T> {
		fun onItemPersisted(item: T?)
	}
}