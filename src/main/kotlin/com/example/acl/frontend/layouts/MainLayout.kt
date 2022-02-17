package com.example.acl.frontend.layouts

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.*
import com.vaadin.flow.router.PageTitle

class MainLayout : AppLayout {
	lateinit var viewTitle: H1

	constructor() {
		primarySection = Section.DRAWER
		addToNavbar(true, createHeaderContent())
		addToDrawer(createDrawerContent())
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

	private fun createDrawerContent(): Component {
		val appName = H2("My App")
		appName.addClassNames("flex", "items-center", "h-xl", "m-0", "px-m", "text-m")
		val section = Section(
			appName,
			createNavigation(), createFooter()
		)
		section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full")
		return section
	}

	private fun createNavigation(): Nav {
		val nav = Nav()
		nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto")
		nav.element.setAttribute("aria-labelledby", "views")

		// Wrap the links in a list; improves accessibility
		val list = UnorderedList()
		list.addClassNames("list-none", "m-0", "p-0")
		nav.add(list)
		val menu = Menu()
		for (menuItem in menu.createMenuItems()) {
			list.add(menuItem)
		}
		return nav
	}



	private fun createFooter(): Footer {
		val layout = Footer()
		layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs")
		return layout
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