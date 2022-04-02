package com.example.acl.frontend.components.dialogs

import com.example.acl.frontend.models.enums.ActionButtons
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.Header
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout


class CustomDialog() : Dialog() {
	constructor(title: String, content: Component) : this() {

		this.element.setAttribute("aria-label", title)
		this.removeAll()

		// Header
		val header = Header()
		header.style
			.set("align-items", "center")
			.set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
		val txtTitle = H2(title)
		txtTitle.style["margin"] = "0"
		val icClose = VaadinIcon.CLOSE.create()
		icClose.setSize("var(--lumo-icon-size-m)")
		icClose.element
			.setAttribute("aria-hidden", "true")
		icClose.style
			.set("box-sizing", "border-box")
			.set("margin-right", "var(--lumo-space-m)")["padding"] = "calc(var(--lumo-space-xs) / 2)"
		val btnClose = Button(ActionButtons.CLOSE.label, icClose)
		btnClose.addClickListener { this.close() }
		header.add(txtTitle)
		this.add(HorizontalLayout(btnClose, header))

		val scroller = CustomScroller(content) { this.close() }
		this.add(scroller)

		this.width = "70%"
		this.height = "70%"
		this.isResizable = true
		this.isDraggable = true
	}

}
