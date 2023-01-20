package com.example.acl.frontend.layouts

import com.example.acl.domains.home.controllers.views.AdminHomeView
import com.example.acl.domains.users.views.roles.RoleFlowView
import com.example.acl.domains.users.views.users.UserFlowView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.dependency.NpmPackage
import com.vaadin.flow.component.html.ListItem
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.router.RouterLink


class Menu {

	fun createMenuItems(): Array<MenuItemInfo> {
		return arrayOf(
			MenuItemInfo("Home", "la la-globe", AdminHomeView::class.java),  //
			MenuItemInfo("Users", "la la-globe", UserFlowView::class.java),  //
			MenuItemInfo("Roles", "la la-globe", RoleFlowView::class.java),  //
		)
	}

	/**
	 * A simple navigation item component, based on ListItem element.
	 */
	class MenuItemInfo(menuTitle: String, iconClass: String, private val view: Class<out Component>) :
		ListItem() {
		init {
			val link = RouterLink()
			// Use Lumo classnames for various styling
			link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary")
			link.setRoute(view)
			val text = Span(menuTitle)
			// Use Lumo classnames for various styling
			text.addClassNames("font-medium", "text-s")
			link.add(LineAwesomeIcon(iconClass), text)
			add(link)
		}

		fun getView(): Class<*> {
			return view
		}

		/**
		 * Simple wrapper to create icons using LineAwesome iconset. See
		 * https://icons8.com/line-awesome
		 */
		@NpmPackage(value = "line-awesome", version = "1.3.0")
		class LineAwesomeIcon(lineawesomeClassnames: String) : Span() {
			init {
				// Use Lumo classnames for suitable font size and margin
				addClassNames("me-s", "text-l")
				if (lineawesomeClassnames.isNotEmpty()) {
					addClassNames(lineawesomeClassnames)
				}
			}
		}
	}
}
