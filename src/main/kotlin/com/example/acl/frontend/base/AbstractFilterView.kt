package com.example.acl.frontend.base

import com.example.acl.frontend.components.layouts.FormLayout
import com.example.acl.frontend.models.enums.ActionButtons
import com.vaadin.flow.component.AttachEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

abstract class AbstractFilterView<T> : Div() {
	private var formLayout: FormLayout = FormLayout()
	private var buttonLayout: HorizontalLayout
	lateinit var btnSubmit: Button
	lateinit var btnClear: Button

	var listener: FilterListener? = null

	init {
		this.formLayout.setSizeFull()
		this.buttonLayout = this.createButtonLayout()

		this.add(this.formLayout, this.buttonLayout)
	}

	override fun onAttach(attachEvent: AttachEvent?) {
		super.onAttach(attachEvent)

		this.initForm(this.formLayout)
		this.formLayout.initialize(true)
	}

	abstract fun initForm(formLayout: FormLayout)

	private fun createButtonLayout(): HorizontalLayout {
		val buttonLayout = HorizontalLayout()
//		buttonLayout.className = "w-full flex-wrap bg-contrast-5 py-s px-l"
//		buttonLayout.isSpacing = true

		this.btnClear = Button(ActionButtons.CLEAR.label)
		this.btnClear.setId(ActionButtons.CLEAR.id)
		this.btnClear.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
		this.btnClear.addClickListener {
			this.formLayout.initialize(true)
		}

		this.btnSubmit = Button(ActionButtons.SUBMIT.label)
		this.btnSubmit.setId(ActionButtons.SUBMIT.id)
		this.btnSubmit.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
		this.btnSubmit.addClickListener {
			this.listener?.onFilterSubmitted(this.formLayout.getValues())
		}

		buttonLayout.add(this.btnSubmit, this.btnClear)
		return buttonLayout
	}


	fun setFilterListener(listener: FilterListener) {
		this.listener = listener
	}

	interface FilterListener {
		fun onFilterSubmitted(result: Map<String, Any?>)
	}
}
