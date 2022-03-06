package com.example.acl.frontend.views

import com.example.acl.frontend.models.enums.Delimiters
import com.vaadin.flow.component.html.Span

class ErrorView {
	var span: Span
	var message: Pair<String, String>

	constructor(message: Pair<String, String>) {
		this.message = message
		this.span = Span(this.getMessage())
		this.span.addClassName("text-error")
	}

	fun getMessage(): String {
		return this.message.first + Delimiters.COLON.value + " " + this.message.second
	}
}