package com.example.acl.frontend.utils

import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout


class Notifications private constructor() {

	companion object {
		@JvmStatic
		fun success(message: String) {
			show(message, NotificationVariant.LUMO_SUCCESS)
		}

		@JvmStatic
		fun info(message: String) {
			show(message, NotificationVariant.LUMO_PRIMARY)
		}

		@JvmStatic
		fun error(message: String) {
			show(message, NotificationVariant.LUMO_ERROR)
		}

		@JvmStatic
		fun show(message: String, type: NotificationVariant) {
			val notification = Notification("Notification", 5000, Notification.Position.TOP_CENTER)
			notification.addThemeVariants(type)

			val text = Div(Text(message))

			val layout = HorizontalLayout(
				text, this.getCloseButton(notification)
			)

			layout.alignItems = FlexComponent.Alignment.CENTER

			notification.add(layout)
			notification.open()
		}

		private fun getCloseButton(notification: Notification): Button {
			val closeButton = Button(Icon("lumo", "cross"))
			closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE)
			closeButton.element.setAttribute("aria-label", "Close")
			closeButton.addClickListener { notification.close() }
			return closeButton
		}
	}

}