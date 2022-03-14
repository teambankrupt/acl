package com.example.acl.frontend.components

import com.example.acl.frontend.models.FieldValidator
import com.example.acl.frontend.models.ValidationResult
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasStyle

interface AbstractInputV2<V> {
	var fieldValidator: FieldValidator<V>?

//	fun label(): String
//	fun label(label: String)

	fun setVal(value: V)
	fun getVal(): V?
	fun clearVal()
	fun getComponent(): Component

	fun getValidator(): FieldValidator<V>?

	fun setMessage(error: Boolean, message: String?)

	fun resolveClass(input: HasStyle, error: Boolean) {
		if (error)
			input.addClassName("field-error")
		else
			input.removeClassName("field-error")
	}

	fun validateInput(): ValidationResult<V> {
		val validator = this.getValidator() ?: return ValidationResult(true, "No validator defined.", this.getVal())
		val result = ValidationResult(
			validator.validator.test(this.getVal()),
			validator.message,
			getVal()
		)
		this.setMessage(true, result.message)
		return result
	}
}