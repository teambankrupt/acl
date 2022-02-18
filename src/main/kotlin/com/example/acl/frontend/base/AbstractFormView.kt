package com.example.acl.frontend.base

import com.example.acl.frontend.components.AbstractInput
import com.example.acl.frontend.components.GenericValueInput
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasStyle
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import java.lang.reflect.Field
import java.time.Instant

abstract class AbstractFormView<T>(klass: Class<T>) : Div() {
	private var klass: Class<T>
	private var fields: List<Field> = listOf()
	private var editMode: Boolean = false
	private var binder: Binder<T>

	var formLayout: FormLayout
	var buttonLayout: HorizontalLayout

	init {
		this.klass = klass
		this.binder = Binder<T>(klass)
		val formFields = this.defineFormFields()

		this.formLayout = this.createEditorLayout(formFields)
		this.add(formLayout)

		this.buttonLayout = this.createButtonLayout()
		this.add(buttonLayout)

	}

	fun getBinder(): Binder<T> {
		return this.binder
	}

	abstract fun defineFormFields(): Map<String, AbstractInput>?

	fun createEditorLayout(formFields: Map<String, AbstractInput>?): FormLayout {
		val showAllFields = formFields.isNullOrEmpty()

		this.className = "flex flex-col space-s"
		this.width = "400px"
		val editorDiv = Div()
		editorDiv.className = "p-l flex-grow"
		this.add(editorDiv)
		val formLayout = FormLayout()

		this.fields = this.fields.ifEmpty { klass.declaredFields.map { it } }

		if (!showAllFields) {
			formFields!!.forEach {
				val field = this.fields.find { f -> f.name == it.key }
				if (field != null) {
					formLayout.add(this.createInput(field, it.value))
				}
			}
		} else {
			this.fields.forEach {
				formLayout.add(this.createInput(it, GenericValueInput(it.name, it.name)))
			}
		}

		return formLayout
	}


	private fun createInput(field: Field, ai: AbstractInput): Component {

		val component = ai.getComponent()
		if (component != null) {
			component.setId(field.name)
			component.onEnabledStateChanged(this.editMode)
//			binder.bind(component,field.name)
			return component
		}

		if (field.type.isEnum) {
			val input = Select<String>()
			input.label = ai.getLabel()
			val items = field.type.enumConstants.map { i -> i.toString() }
			input.setItems(items)
			input.setId(field.name)
			input.isEnabled = this.editMode
			(input as HasStyle).addClassName("full-width")
			binder.bind(input, field.name)
			return input
		} else if (field.type == Instant::class.java) {
			val input = DatePicker(ai.getLabel())
			input.setId(field.name)
			input.isEnabled = this.editMode
			(input as HasStyle).addClassName("full-width")
			binder.bind(input, field.name)
			return input
		} else if (field.type == Boolean::class.java) {
			val input = Checkbox(ai.getLabel())
			input.setId(field.name)
			input.isEnabled = this.editMode
			(input as HasStyle).addClassName("full-width")
			binder.bind(input, field.name)
			return input
		}

		val input = TextField(ai.getLabel())
		input.setId(field.name)
		input.isEnabled = this.editMode
		(input as HasStyle).addClassName("full-width")
		binder.bind(input, field.name)
		return input
	}

	private fun createButtonLayout(): HorizontalLayout {
		val buttonLayout = HorizontalLayout()
		buttonLayout.className = "w-full flex-wrap bg-contrast-5 py-s px-l"
		buttonLayout.isSpacing = true
		val cancel = Button("Cancel")
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
		val btnSave = Button("Save")
		btnSave.addClickListener { this.onSaveClicked(btnSave, it) }
		btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
		this.resolveBtnState(btnSave)
		buttonLayout.add(btnSave, cancel)
		return buttonLayout
	}

	private fun onSaveClicked(btnSave: Button, event: ClickEvent<Button>?) {
		this.editMode = !this.editMode
		this.reInitializeLayout()
//		this.resolveBtnState(btnSave)
	}

	private fun resolveBtnState(button: Button) {
		button.text = if (this.editMode) "Save" else "Update"
	}

	private fun reInitializeLayout() {
		this.remove(this.formLayout, this.buttonLayout)
		this.formLayout = this.createEditorLayout(this.defineFormFields())
		this.buttonLayout = this.createButtonLayout()
		this.add(this.formLayout, this.buttonLayout)
	}

	abstract fun onItemSelected(selected: Boolean, item: T?)
}