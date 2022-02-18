package com.example.acl.frontend.components

import com.vaadin.flow.component.Component

class AutoCompleteTextField : AbstractInput {
	private var label: String
	private var placeholder: String
	private var fieldName: String
	private var defaultValue: String? = null
	private var component: Component? = null

	constructor(
		fieldName: String,
		label: String,
		component: Component?
	) {
		this.fieldName = fieldName
		this.label = label
		this.placeholder = label
		this.component = component
	}

	constructor(
		fieldName: String,
		label: String,
		placeholder: String,
		defaultValue: String?,
		component: Component?
	) {
		this.fieldName = fieldName
		this.label = label
		this.placeholder = placeholder
		this.defaultValue = defaultValue
		this.component = component
	}

	override fun getLabel(): String {
		return this.label
	}

	override fun getPlaceholder(): String {
		return this.placeholder
	}

	override fun getFieldName(): String {
		return this.fieldName
	}

	override fun getDefaultValue(): String? {
		return this.defaultValue
	}

	override fun getComponent(): Component? {
		return component
	}


}