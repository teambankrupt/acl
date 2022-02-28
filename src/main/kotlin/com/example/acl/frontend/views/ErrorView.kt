package com.example.acl.frontend.views

import com.vaadin.flow.component.html.Span

class ErrorView {
	var message: String
	var span: Span

	constructor(message: String) {
		this.message = message
		this.span = Span(message)
	}
}