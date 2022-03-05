package com.example.acl.frontend.views

import com.vaadin.flow.component.html.Span

class ErrorView {
	var span: Span
	var message: Pair<String, String>

	constructor(message: Pair<String, String>) {
		this.message = message
		this.span = Span(message.second)
	}

	fun getMessage(): String {
		return this.message.first + " " + this.message.second
	}
}