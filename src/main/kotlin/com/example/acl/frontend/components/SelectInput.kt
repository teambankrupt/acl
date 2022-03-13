package com.example.acl.frontend.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.select.Select

class SelectInput<T>() : Select<T>(), AbstractInputV2<T> {

	constructor(id: String, label: String, items: List<T>) : this() {
		this.setId(id)
		this.label = label
		this.setItems(items)
		this.addValueChangeListener {
			this.setVal(it.value)
		}
	}

	override fun setVal(value: T) {
		this.value = value
	}

	override fun getVal(): T? {
		return this.value
	}

	override fun clearVal() {
		this.clear()
	}

	override fun getComponent(): Component {
		return this
	}
}