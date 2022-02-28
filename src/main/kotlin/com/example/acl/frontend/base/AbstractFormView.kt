package com.example.acl.frontend.base

import com.example.acl.frontend.components.AbstractInput
import com.example.acl.frontend.components.GenericValueInput
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.HasStyle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.EmailField
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.Validator
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

	private var formLayout: FormLayout
	private var inputComponents: MutableMap<String, AbstractField<*, *>>
	private var buttonLayout: HorizontalLayout
	lateinit var btnSave: Button
	lateinit var btnCancel: Button
	private var errorMsgLayout: Div = Div()

	var itemPersistenceListener: ItemPersistenceListener<T>? = null

	init {
		this.klass = klass
		this.binder = Binder<T>(klass)
		this.choosableValues = mutableMapOf()
		this.inputComponents = mutableMapOf()

		this.errorMsgLayout.isVisible = false
		val errTitle = Span("Validation Errors")
		errTitle.element.themeList.add("badge error")
		this.errorMsgLayout.add(errTitle)
		this.add(errorMsgLayout)

		this.formLayout = FormLayout()

		this.buttonLayout = this.createButtonLayout()
	}


	fun initForm(components: MutableMap<String, AbstractField<*, *>>) {
		val formFields = this.defineFormFields()
		this.inputComponents = components.ifEmpty { this.createFormInputs(formFields) }
		this.inputComponents.forEach { this.formLayout.add(it.value) }

		this.add(this.formLayout, this.buttonLayout)

	}

	fun getBinder(): Binder<T> {
		return this.binder
	}

	fun setSelected(selected: Boolean) {
		this.hasSelectedItem = selected
		this.resolveBtnState(this.btnSave, this.btnCancel)
		this.setDefaultSelectFieldValues(this.getDefaultSingleSelectionValues())
	}

	abstract fun getDefaultSingleSelectionValues(): Map<String, String>

	private fun setDefaultSelectFieldValues(choosableValues: Map<String, String>) {
		choosableValues.forEach {
			val component = this.inputComponents[it.key] as Select<*>
			component.value = it.value
			this.inputComponents[it.key] = component
		}
	}

	abstract fun defineFormFields(): Map<String, AbstractInput<T>>?

	fun createFormInputs(formFields: Map<String, AbstractInput<T>>?): MutableMap<String, AbstractField<*, *>> {
		val showAllFields = formFields.isNullOrEmpty()
		val inputComponents: MutableMap<String, AbstractField<*, *>> = mutableMapOf()

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
				inputComponents[it.name] = this.createInput(it, GenericValueInput(it.name, it.name, null))
			}
		}

		return inputComponents
	}


	private fun createInput(field: Field, ai: AbstractInput<T>): AbstractField<*, *> {

		val component = ai.getComponent()
		if (component != null) {
			component.setId(field.name)
			component.isReadOnly = !this.editMode
//			binder.bind(component,field.name)
			return component
		}

		if (field.type.isEnum) {
			val input = Select<String>()
			input.label = ai.getLabel()
			val items = field.type.enumConstants.map { i -> i.toString() }
			input.setItems(items)
			input.addValueChangeListener {
				this.choosableValues[field.name] = it.value
			}
			this.configure(field, input, ai, false)
//			binder.forField(input)
//				.withConverter(StringToEnumConverter(field.type))
//				.bind(field.name)
			return input
		} else if (field.type == Instant::class.java) {
			val input = DatePicker(ai.getLabel())
			this.configure(field, input, ai, true)
			return input
		} else if (field.type == Boolean::class.java) {
			val input = Checkbox(ai.getLabel())
			this.configure(field, input, ai, true)
			return input
		} else if (
			field.type == Int::class.java
			|| field.type == Long::class.java
			|| field.type == Double::class.java
		) {
			val input = NumberField(ai.getLabel())
			this.configure(field, input, ai, true)
			return input
		}

		val input = if (this.containsAnnotations(field, "javax.validation.constraints.Email"))
			EmailField(ai.getLabel())
		else if (field.name.lowercase().contains("password"))
			PasswordField(ai.getLabel())
		else
			TextField(ai.getLabel())

		this.configure(field, input, ai, true)
		return input
	}

	fun configure(field: Field, input: AbstractField<*, *>, ai: AbstractInput<T>, bind: Boolean) {
		input.setId(field.name)
		input.isReadOnly = !this.editMode
		(input as HasStyle).addClassName("full-width")
		if (bind) {
			if (ai.getValidator() != null)
				binder.withValidator(Validator.from(ai.getValidator(), ai.getErrorView().message))
			binder.bind(input, field.name)
		}
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
				this.getBinder().readBean(null)
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
		if (!btnId.isPresent) return

		if (this.editMode) {
			if (btnId.get() == "id_save") {
				if (this.validateBean())
					return
				this.onSaveAction(event, this.choosableValues)
			} else if (btnId.get() == "id_cancel") {
				this.errorMsgLayout.removeAll()
				this.errorMsgLayout.isVisible = false
				this.onCancelAction(event)
			}
		}

		this.editMode = !this.editMode
		this.reInitializeLayout()


	}

	private fun validateBean(): Boolean {
		this.getBinder().bean = this.getBean()
		val result = this.getBinder().validate()
		val hasErrors = result.hasErrors()
		if (hasErrors) {
			this.errorMsgLayout.isVisible = true
			result.validationErrors.forEach {
				val msg = Span()
				msg.element.setProperty("innerHTML", "<br/>* " + it.errorMessage)
				msg.addClassName("text-error")
				this.errorMsgLayout.add(msg)
			}
		} else {
			this.errorMsgLayout.removeAll()
			this.errorMsgLayout.isVisible = false
		}
		return hasErrors
	}

	abstract fun getBean(): T

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
			it.value.isReadOnly = !this.editMode
			it.value
		}.toMutableMap()
		this.inputComponents.forEach { this.formLayout.add(it.value) }

		this.remove(this.buttonLayout)
		this.buttonLayout = this.createButtonLayout()
		this.add(this.formLayout, this.buttonLayout)
	}

	abstract fun onItemSelected(selected: Boolean, item: T?)

	fun setItemPersistedListener(listener: ItemPersistenceListener<T>) {
		this.itemPersistenceListener = listener
	}

	fun containsAnnotations(field: Field, annotationQualifiedName: String): Boolean {
		return field.declaredAnnotations
			.any { it.annotationClass.qualifiedName == annotationQualifiedName }
	}

	interface ItemPersistenceListener<T> {
		fun onItemPersisted(item: T?)
	}
}