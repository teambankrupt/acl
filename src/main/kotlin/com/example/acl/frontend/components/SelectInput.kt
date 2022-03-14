package com.example.acl.frontend.components

import com.example.acl.frontend.models.FieldValidator
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.select.Select

class SelectInput<T>(
	override var fieldValidator: FieldValidator<T>?
) : Select<T>(), AbstractInputV2<T> {

	constructor(id: String, label: String, items: List<T>,fieldValidator: FieldValidator<T>?) : this(fieldValidator) {
		this.setId(id)
		this.label = label
		this.setItems(items)
		this.addValueChangeListener {
			this.setVal(it.value)
		}
	}

	override fun setVal(value: T) {
		this.value = value
	}

	override fun getVal(): T? {
		return this.value
	}

	override fun clearVal() {
		this.clear()
	}

	override fun getComponent(): Component {
		return this
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