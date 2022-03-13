package com.example.acl.frontend.components

import com.example.acl.frontend.models.FieldValidator
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.textfield.TextField

class TextInput(
	override var fieldValidator: FieldValidator<String>?
) : TextField(), AbstractInputV2<String> {

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

	override fun setErrMessage(errorMsg: String) {
		this.errorMessage = errorMsg
		this.label = this.label + "\n($errorMsg)"
	}


}