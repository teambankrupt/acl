package com.example.acl.frontend.components

import com.example.acl.frontend.models.FieldValidator
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField

class NumberInput(
	override var fieldValidator: FieldValidator<Double>?
) : NumberField(), AbstractInputV2<Double> {

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

	override fun setErrMessage(errorMsg: String) {
		this.errorMessage = errorMsg
		this.label = this.label + "\n($errorMsg)"
	}


}