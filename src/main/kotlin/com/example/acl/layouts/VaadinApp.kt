package com.example.acl.layouts

import com.vaadin.flow.component.dependency.NpmPackage
import com.vaadin.flow.component.dependency.NpmPackage.Container
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@PWA(name = "OpenPB Dashboard", shortName = "OpenPB Dashboard")
@Theme("admintheme", variant = Lumo.DARK)
@Container(
	value = [
		NpmPackage(value = "@fontsource/poppins", version = "4.5.0"),
		NpmPackage(value = "lumo-css-framework", version = "^4.0.10"),
		NpmPackage(value = "line-awesome", version = "1.3.0")
	]
)
class VaadinApp : AppShellConfigurator {
}