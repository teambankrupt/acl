package com.example.acl.frontend.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.textfield.TextField

class TextInput() : TextField(), AbstractInputV2<String> {

	constructor(id: String, label: String) : this() {
		this.setId(id)
		this.label = label
		this.placeholder = label
	}

	override fun setVal(value: String) {
		this.value = value
	}

	override fun getVal(): String? {
		return this.value
	}

	override fun getComponent(): Component {
		return this
	}

	override fun clearVal() {
		this.clear()
	}

}