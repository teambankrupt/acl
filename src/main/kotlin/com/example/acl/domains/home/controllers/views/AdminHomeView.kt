package com.example.acl.domains.home.controllers.views

import com.example.acl.frontend.layouts.MainLayout
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route(value = "", layout = MainLayout::class)
//@PageTitle("Admin Dashboard")
class AdminHomeView : VerticalLayout {
	constructor() {
		add(Text("Hello World"))
	}
}