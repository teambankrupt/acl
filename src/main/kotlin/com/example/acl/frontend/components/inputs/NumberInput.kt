package com.example.acl.frontend.components.inputs

import com.example.acl.frontend.models.FieldValidator
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.textfield.NumberField

class NumberInput(
	override var fieldValidator: FieldValidator<Double>?
) : NumberField(), AbstractInput<Double> {

	constructor(id: String, label: String, fieldValidator: FieldValidator<Double>?) : this(fieldValidator) {
		this.setId(id)
		this.label = label
		this.placeholder = label
	}

	override fun setVal(value: Double) {
		this.value = value
	}

	override fun getVal(): Double? {
		return this.value
	}

	override fun getComponent(): Component {
		return this
	}

	override fun clearVal() {
		this.clear()
	}

	override fun getValidator(): FieldValidator<Double>? {
		return this.fieldValidator
	}

	override fun setMessage(error: Boolean, message: String?) {
		this.resolveClass(this, error)
		if (message != null) this.label = message
		this.errorMessage = message
	}


}