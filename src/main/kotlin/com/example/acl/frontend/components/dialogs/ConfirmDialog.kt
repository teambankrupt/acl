package com.example.acl.frontend.components.dialogs

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Hr
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout

class ConfirmDialog() : Dialog() {

	constructor(
		title: String, message: String,
		confirmAction: (paramObject: Any?) -> Unit,
		paramObject: Any?
	) : this() {
		val layout = VerticalLayout()
		layout.add(H1(title))
		layout.add(Hr())
		layout.add(Paragraph(message))

		val buttonLayout = HorizontalLayout()
		val btnCancel = Button("Cancel")
		btnCancel.addClickListener { this.close() }
		val btnConfirm = Button("Confirm")
		btnConfirm.addClickListener {
			confirmAction(paramObject)
			this.close()
		}
		buttonLayout.add(btnCancel, btnConfirm)
		layout.add(buttonLayout)
	}

}