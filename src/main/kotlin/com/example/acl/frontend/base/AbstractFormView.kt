package com.example.acl.frontend.base

import com.example.acl.frontend.components.AbstractInput
import com.example.acl.frontend.components.GenericValueInput
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasStyle
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
import java.util.*

abstract class AbstractFormView<T>(klass: Class<T>) : Div() {
	private var klass: Class<T>
	private var fields: List<Field> = listOf()
	private var editMode: Boolean = false
	private var hasSelectedItem: Boolean = false

	private var binder: Binder<T>
	private var choosableValues: MutableMap<String, String>

	var formLayout: FormLayout
	var inputComponents: MutableMap<String, Component>
	var buttonLayout: HorizontalLayout
	lateinit var btnSave: Button
	lateinit var btnCancel: Button

	init {
		this.klass = klass
		this.binder = Binder<T>(klass)
		this.choosableValues = mutableMapOf()
		this.inputComponents = mutableMapOf()
		this.formLayout = FormLayout()

		val formFields = this.defineFormFields()

		this.inputComponents = this.createFormInputs(formFields)
		this.inputComponents.forEach { this.formLayout.add(it.value) }

		this.add(formLayout)

		this.buttonLayout = this.createButtonLayout()
		this.add(buttonLayout)

	}

	fun getBinder(hasSelectedItem: Boolean): Binder<T> {
		this.hasSelectedItem = hasSelectedItem
		this.resolveBtnState(this.btnSave, this.btnCancel)
		this.setDefaultSelectFieldValues(this.getDefaultSelectValues())
		return this.binder
	}

	abstract fun getDefaultSelectValues(): Map<String, String>

	private fun setDefaultSelectFieldValues(choosableValues: Map<String, String>) {
		choosableValues.forEach {
			val component = this.inputComponents[it.key] as Select<*>
			component.value = it.value
			this.inputComponents[it.key] = component
		}
	}

	abstract fun defineFormFields(): Map<String, AbstractInput>?

	fun createFormInputs(formFields: Map<String, AbstractInput>?): MutableMap<String, Component> {
		val showAllFields = formFields.isNullOrEmpty()
		val inputComponents: MutableMap<String, Component> = mutableMapOf()

		this.className = "flex flex-col space-s"
		this.width = "400px"
		val editorDiv = Div()
		editorDiv.className = "p-l flex-grow"
		this.add(editorDiv)

		this.fields = this.fields.ifEmpty { klass.declaredFields.map { it } }

		if (!showAllFields) {
			formFields!!.forEach {
				val field = this.fields.find { f -> f.name == it.key }
				if (field != null) {
					inputComponents[field.name] = (this.createInput(field, it.value))
				}
			}
		} else {
			this.fields.forEach {
				inputComponents[it.name] = this.createInput(it, GenericValueInput(it.name, it.name))
			}
		}

		return inputComponents
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
			input.addValueChangeListener {
				this.choosableValues[field.name] = it.value
			}
//			binder.forField(input)
//				.withConverter(StringToEnumConverter(field.type))
//				.bind(field.name)
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

		this.btnCancel = Button("Reset")
		this.btnCancel.setId("id_cancel")
		this.btnCancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
		this.btnCancel.addClickListener {
			if (this.isFormResettable()) {
				this.getBinder(false).readBean(null)
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

		this.resolveBtnState(this.btnSave)
		buttonLayout.add(this.btnSave, this.btnCancel)
		return buttonLayout
	}

	fun isFormResettable(): Boolean {
		return !this.editMode
	}

	private fun onButtonClicked(btnId: Optional<String>, event: ClickEvent<Button>) {
		btnId.ifPresent {

			if (this.editMode) {
				if (it == "id_save") {
					this.onSaveAction(event, this.choosableValues)
				} else if (it == "id_cancel") {
					this.onCancelAction(event)
				}
			}

			this.editMode = !this.editMode
			this.reInitializeLayout()

		}

	}

	abstract fun onSaveAction(event: ClickEvent<Button>, dropdownValues: MutableMap<String, String>)
	abstract fun onCancelAction(event: ClickEvent<Button>)

	private fun resolveBtnState(vararg buttons: Button) {
		buttons.forEach {

			if (it.id.get() == "id_save") {
				it.text = if (this.editMode) "Save" else {
					if (this.hasSelectedItem) "Update" else "Create New"
				}
			} else if (it.id.get() == "id_cancel") {
				it.text = if (this.isFormResettable())
					"Reset" else "Cancel"
			}

		}
	}

	private fun reInitializeLayout() {
		// clear forms
		this.formLayout.removeAll()
		this.choosableValues.clear()


		this.inputComponents = this.inputComponents.mapValues {
			it.value.onEnabledStateChanged(this.editMode)
			it.value
		}.toMutableMap()
		this.inputComponents.forEach { this.formLayout.add(it.value) }

		this.remove(this.buttonLayout)
		this.buttonLayout = this.createButtonLayout()
		this.add(this.formLayout, this.buttonLayout)
	}

	abstract fun onItemSelected(selected: Boolean, item: T?)
}