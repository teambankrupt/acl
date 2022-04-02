package com.example.acl.frontend.components.dialogs

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.icon.VaadinIcon
import com.example.acl.frontend.components.dialogs.CustomScroller
import com.example.acl.frontend.models.enums.ActionButtons
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.*
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import java.time.LocalDate

class CustomScroller(content: Component, onClose: () -> Unit) : VerticalLayout() {

	init {
		alignItems = FlexComponent.Alignment.STRETCH
		height = "90%"
		width = "100%"
		isPadding = false
		isSpacing = false
		style["border"] = "1px solid var(--lumo-contrast-20pct)"

		val scroller = Scroller(Div(content))
		scroller.scrollDirection = Scroller.ScrollDirection.VERTICAL
		scroller.style
			.set("border-bottom", "1px solid var(--lumo-contrast-20pct)")["padding"] = "var(--lumo-space-m)"
		add(scroller)

	}


}
