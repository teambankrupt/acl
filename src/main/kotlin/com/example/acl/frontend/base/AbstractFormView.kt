package com.example.acl.frontend.base

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
import java.lang.reflect.Field
import java.time.Instant

abstract class AbstractFormView<T>(klass: Class<T>) : Div() {
	private var klass: Class<T>
	private var fields: List<Field> = listOf()

	init {
		this.klass = klass
		val formFields = this.defineFormFields()
		this.createEditorLayout(formFields)
	}

	abstract fun defineFormFields(): Map<String, String>?

	fun createEditorLayout(formFields: Map<String, String>?) {
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
				formLayout.add(this.createInput(it, it.name))
			}
		}

		this.add(formLayout)
		this.add(this.createButtonLayout())
	}


	private fun createInput(field: Field, label: String): Component {
		if (field.type.isEnum) {
			val input = Select<String>()
			input.label = label
			val items = field.type.enumConstants.map { i -> i.toString() }
			input.setItems(items)
			input.setId(field.name)
			(input as HasStyle).addClassName("full-width")
			return input
		} else if (field.type == Instant::class.java) {
			val input = DatePicker(label)
			input.setId(field.name)
			(input as HasStyle).addClassName("full-width")
			return input
		} else if (field.type == Boolean::class.java) {
			val input = Checkbox(label)
			input.setId(field.name)
			(input as HasStyle).addClassName("full-width")
			return input
		}

		val input = TextField(label)
		input.setId(field.name)
		return input
	}

	private fun createButtonLayout(): HorizontalLayout {
		val buttonLayout = HorizontalLayout()
		buttonLayout.className = "w-full flex-wrap bg-contrast-5 py-s px-l"
		buttonLayout.isSpacing = true
		val cancel = Button("Cancel")
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
		val save = Button("Save")
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
		buttonLayout.add(save, cancel)
		return buttonLayout
	}

}