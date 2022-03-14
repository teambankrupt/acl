package com.example.acl.frontend.components

import com.example.acl.frontend.models.FieldValidator
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.textfield.EmailField

class EmailInput(
	override var fieldValidator: FieldValidator<String>?
) : EmailField(), AbstractInputV2<String> {

	constructor(id: String, label: String, fieldValidator: FieldValidator<String>?) : this(fieldValidator) {
		this.setId(id)
		this.label = label
		this.placeholder = label
	}

	override fun setVal(value: String) {
		this.value = value
	}

	override fun getVal(): String? {
		return this.value
	}

	override fun getComponent(): Component {
		return this
	}

	override fun clearVal() {
		this.clear()
	}

	override fun getValidator(): FieldValidator<String>? {
		return this.fieldValidator
	}

	override fun setMessage(error: Boolean, message: String?) {
		this.resolveClass(this, error)
		if (message != null) this.label = message
		this.errorMessage = message
	}
}