package com.example.acl.frontend.models

import com.vaadin.flow.function.SerializablePredicate

class FieldValidator<T>(
	val validator: SerializablePredicate<T>,
	val message: String
) {
}