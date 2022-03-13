package com.example.acl.frontend.components

import com.example.acl.frontend.models.ValidationResult
import com.vaadin.flow.component.Component
import com.vaadin.flow.function.SerializablePredicate

interface AbstractInputV2<V> {
	fun setUID(id: String)
	fun getUID(): String
	fun setVal(value: V)
	fun getVal(): V?
	fun getComponent(): Component
	fun validate(predicate: SerializablePredicate<V>, message: String): ValidationResult<V> {
		return ValidationResult(
			predicate.test(this.getVal()),
			message,
			getVal()
		)
	}
}