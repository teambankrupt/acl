package com.example.acl.frontend.layouts

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.*
import com.vaadin.flow.router.PageTitle

class FullScreenLayout : AppLayout {
	lateinit var viewTitle: H1

	constructor() {
		primarySection = Section.NAVBAR
		addToNavbar(true, createHeaderContent())
	}

	private fun createHeaderContent(): Component {
		val toggle = DrawerToggle()
		toggle.addClassName("text-secondary")
		toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST)
		toggle.element.setAttribute("aria-label", "Menu toggle")
		viewTitle = H1()
		viewTitle.addClassNames("m-0", "text-l")
		val header = Header(toggle, viewTitle)
		header.addClassNames(
			"bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-center",
			"w-full"
		)
		return header
	}

	override fun afterNavigation() {
		super.afterNavigation()
		viewTitle.text = getCurrentPageTitle()
	}


	private fun getCurrentPageTitle(): String {
		val annotation = content.javaClass.getAnnotation(PageTitle::class.java)
			?: return content.javaClass.simpleName
		return annotation.value
	}
}
