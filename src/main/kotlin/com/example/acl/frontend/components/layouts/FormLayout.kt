package com.example.acl.frontend.components.layouts

import com.example.acl.frontend.components.inputs.AbstractInput
import com.example.common.utils.ExceptionUtil
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.HasStyle

class FormLayout : com.vaadin.flow.component.formlayout.FormLayout() {
	var inputs: MutableList<AbstractInput<*>> = mutableListOf()

	fun addInputs(inputs: List<AbstractInput<*>>) {
		this.inputs.addAll(inputs)
	}

	fun initialize(editMode: Boolean) {
		this.removeAll()
		this.inputs.forEach {
			val component = it.getComponent()
			if (component is AbstractField<*, *>)
				component.isReadOnly = !editMode
			if (component is HasStyle)
				(component as HasStyle).addClassName("full-width")
			this.add(component)
		}
	}

	fun getValues(): Map<String, Any?> {
		return this.inputs.associate {
			val id =
				it.getComponent().id.orElseThrow { ExceptionUtil.invalid("FormLayout.getValues(): Component ${it.toString()} must have an unique id!") }
			id to it.getVal()
		}
	}

	fun resetValues() {
		this.inputs.forEach {
			it.clearVal()
		}
	}
}