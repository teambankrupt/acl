package com.example.acl.frontend.components

import com.example.acl.frontend.models.FieldValidator
import com.example.acl.frontend.models.ValidationResult
import com.vaadin.flow.component.Component
import com.vaadin.flow.function.SerializablePredicate

interface AbstractInputV2<V> {
	var fieldValidator: FieldValidator<V>?

	fun setVal(value: V)
	fun getVal(): V?
	fun clearVal()
	fun getComponent(): Component

	fun getValidator(): FieldValidator<V>?

	fun setErrMessage(errorMsg: String)

	fun validateInput(): ValidationResult<V> {
		val validator = this.getValidator() ?: return ValidationResult(true, "No validator defined.", this.getVal())
		val result = ValidationResult(
			validator.validator.test(this.getVal()),
			validator.message,
			getVal()
		)
		if (!result.valid)
			this.setErrMessage(result.message)
		return result
	}
}