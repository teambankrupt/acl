package com.example.acl.frontend.base

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.splitlayout.SplitLayout
import java.lang.reflect.Field


abstract class AbstractFlowView<T> : Div() {
	private lateinit var klass: Class<T>
	private var fields: List<Field> = listOf()

	fun initialize(
		klass: Class<T>,
		browseView: AbstractBrowseView<T>,
		formView: AbstractFormView<T>
	) {
		this.klass = klass
		this.fields = this.fields.ifEmpty { klass.declaredFields.map { it } }

		addClassNames("flex", "flex-col", "h-full")

		val splitLayout = SplitLayout()
		splitLayout.setSizeFull()

		splitLayout.addToPrimary(browseView)
		splitLayout.addToSecondary(formView)
		add(splitLayout)
	}

//	abstract fun getFormView(): AbstractFormView<T>
//
//	abstract fun getBrowseView(): AbstractBrowseView<T>


}