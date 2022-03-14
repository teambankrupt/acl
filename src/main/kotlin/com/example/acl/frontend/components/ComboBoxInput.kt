package com.example.acl.frontend.components

import com.example.acl.frontend.models.FieldValidator
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.data.provider.CallbackDataProvider

class ComboBoxInput<T>(
	override var fieldValidator: FieldValidator<T>?
) : ComboBox<T>(), AbstractInputV2<T> {

	constructor(id: String, label: String, validator: FieldValidator<T>?) : this(validator) {
		this.setId(id)
		this.label = label
		this.placeholder = label
		this.fieldValidator = validator
	}

	override fun setVal(value: T) {
		this.value = value
	}

	override fun getVal(): T? {
		return this.value
	}

	override fun getComponent(): Component {
		return this
	}

	fun withItems(values: List<T>): ComboBoxInput<T> {
		this.setItems(values)
		return this
	}

	fun withDataProvider(
		fetchCallback: CallbackDataProvider.FetchCallback<T, String>
	): ComboBoxInput<T> {
		this.setItems(fetchCallback)
		this.setItemLabelGenerator { it.toString() }
		return this
	}

	fun withListener(listener: AcListener<T>): ComboBoxInput<T> {
		this.addValueChangeListener {
			listener.onAcChange(it)
		}
		this.addCustomValueSetListener {
			listener.onAcApplied(it)
		}
		return this
	}

	fun setOptions(options: List<T>) {
		this.setItems(options)
	}

	@FunctionalInterface
	interface AcListener<T> {
		fun onAcChange(event: ComponentValueChangeEvent<ComboBox<T>, T>)
		fun onAcApplied(event: CustomValueSetEvent<ComboBox<T>>)
	}

	override fun clearVal() {
		this.clear()
	}

	override fun getValidator(): FieldValidator<T>? {
		return this.fieldValidator
	}

	override fun setMessage(error: Boolean, message: String?) {
		this.resolveClass(this, error)
		if (message != null) this.label = message
		this.errorMessage = message
	}

}