package com.example.acl.domains.home.views

import com.example.acl.frontend.layouts.FullScreenLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.router.Route

@Route(value = com.example.acl.routing.Route.V1.VAADIN_WEB_LOGIN, layout = FullScreenLayout::class)
class LoginView : Div() {

	init {
		style
			.set("background-color", "var(--lumo-contrast-5pct)")
			.set("display", "flex")
			.set("justify-content", "center")
			.set("padding", "var(--lumo-space-l)");

		val loginForm = LoginForm()
		this.add(loginForm)
		loginForm.addLoginListener {
			println(it.username)
			println(it.password)
		}

		// Prevent the example from stealing focus when browsing the documentation
		loginForm.element.setAttribute("no-autofocus", "");
	}

}
