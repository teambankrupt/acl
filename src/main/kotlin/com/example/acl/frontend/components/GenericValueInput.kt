package com.example.acl.frontend.components

import com.vaadin.flow.component.Component
import java.util.*

class GenericValueInput : AbstractInput {
	private var label: String
	private var placeholder: String
	private var fieldName: String
	private var defaultValue: String? = null

	constructor(
		fieldName: String,
		label: String
	) {
		val title =
			fieldName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
		this.label = title
		this.placeholder = title
		this.fieldName = fieldName
		this.label = label
	}

	constructor(
		fieldName: String,
		label: String,
		placeholder: String,
		defaultValue: String?
	) {
		this.fieldName = fieldName
		this.label = label
		this.placeholder = placeholder
		this.defaultValue = defaultValue
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
		return null
	}

}