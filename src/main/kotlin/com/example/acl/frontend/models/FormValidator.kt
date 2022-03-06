package com.example.acl.frontend.models

import com.vaadin.flow.function.SerializablePredicate

class FormValidator<T>(
	val validator: SerializablePredicate<T>,
	val messageMap: Map<String, String>
) {
}