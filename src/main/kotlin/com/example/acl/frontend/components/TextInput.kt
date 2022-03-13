package com.example.acl.frontend.components

import com.example.common.utils.ExceptionUtil
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.textfield.TextField

class TextInput() : TextField(), AbstractInputV2<String> {

	constructor(id: String, label: String) : this() {
		this.setUID(id)
		this.label = label
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

	override fun setUID(id: String) {
		this.setId(id)
	}

	override fun getUID(): String {
		return this.id.orElseThrow { ExceptionUtil.invalid("TextInput must set unique id.") }
	}

}