package com.example.acl.frontend.components.inputs

import com.example.acl.frontend.models.FieldValidator
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.checkbox.Checkbox

class CheckboxInput(
	override var fieldValidator: FieldValidator<Boolean>?
) : Checkbox(), AbstractInput<Boolean> {

	constructor(id: String, label: String, fieldValidator: FieldValidator<Boolean>?) : this(fieldValidator) {
		this.setId(id)
		this.label = label
		this.setAriaLabel(label)
	}

	override fun setVal(value: Boolean) {
		this.value = value
	}

	override fun getVal(): Boolean? {
		return this.value
	}

	override fun getComponent(): Component {
		return this
	}

	override fun clearVal() {
		this.clear()
	}

	override fun getValidator(): FieldValidator<Boolean>? {
		return null
	}

	override fun setMessage(error: Boolean, message: String?) {
		this.resolveClass(this, error)
		if (message != null) this.label = message
	}


}