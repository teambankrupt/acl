package com.example.acl.frontend.base

import com.example.acl.frontend.components.dialogs.CustomDialog
import com.example.acl.frontend.models.enums.ActionButtons
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Hr
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.splitlayout.SplitLayout
import java.lang.reflect.Field


abstract class AbstractFlowView<T> : Div() {
	private lateinit var klass: Class<T>
	private var fields: List<Field> = listOf()
	lateinit var dialog: CustomDialog

	fun initialize(
		klass: Class<T>,
		filterView: AbstractFilterView<T>,
		browseView: AbstractBrowseView<T>,
		formView: AbstractFormView<T>
	) {
		this.klass = klass
		this.fields = this.fields.ifEmpty { klass.declaredFields.map { it } }
		this.dialog = CustomDialog("", formView)

		addClassNames("flex", "flex-col", "h-full")

		val splitLayout = SplitLayout()
		splitLayout.setSizeFull()

		val filterBrowseView = VerticalLayout()
		filterBrowseView.add(filterView)

		if (isNewButtonEnabled()) {
			filterBrowseView.add(Hr())
			val btnNew = Button(ActionButtons.NEW_ITEM.label)
			btnNew.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
			btnNew.addClickListener { this.dialog.open() }
			val div = Div()
			div.add(btnNew)
			filterBrowseView.add(div)
		}

		filterBrowseView.add(browseView)

		splitLayout.addToPrimary(filterBrowseView)
//		splitLayout.addToSecondary(formView)
		add(splitLayout)

	}

	abstract fun isNewButtonEnabled(): Boolean

}
