package com.example.acl.frontend.components.layouts

import com.example.acl.frontend.components.AbstractInputV2
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.HasStyle

class FormLayout : com.vaadin.flow.component.formlayout.FormLayout() {
	var inputs: MutableList<AbstractInputV2<*>> = mutableListOf()

	fun addInputs(inputs: List<AbstractInputV2<*>>) {
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

	fun getValues(): Map<String, Any?>{
		return this.inputs.associate { it.getUID() to it.getVal() }
	}
}