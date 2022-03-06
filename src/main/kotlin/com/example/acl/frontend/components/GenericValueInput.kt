package com.example.acl.frontend.components

import com.example.acl.frontend.views.ErrorView
import com.vaadin.flow.component.Component
import com.vaadin.flow.function.SerializablePredicate
import java.util.*

open class GenericValueInput<T> : AbstractInput<T> {
	protected var iLabel: String
	protected var iPlaceholder: String
	protected var iFieldName: String
	protected var iDefaultValue: String? = null
	protected var iValidator: SerializablePredicate<in Any>? = null

	constructor(
		fieldName: String,
		label: String,
		validator: SerializablePredicate<in Any>?
	) {
		val title =
			fieldName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
		this.iLabel = title
		this.iPlaceholder = title
		this.iFieldName = fieldName
		this.iLabel = label
		this.iValidator = validator
	}

	constructor(
		fieldName: String,
		label: String,
		placeholder: String,
		defaultValue: String?,
		validator: SerializablePredicate<in Any>?
	) {
		this.iFieldName = fieldName
		this.iLabel = label
		this.iPlaceholder = placeholder
		this.iDefaultValue = defaultValue
		this.iValidator = validator
	}

	override fun getLabel(): String {
		return this.iLabel
	}

	override fun getPlaceholder(): String {
		return this.iPlaceholder
	}

	override fun getFieldName(): String {
		return this.iFieldName
	}

	override fun getDefaultValue(): String? {
		return this.iDefaultValue
	}

	override fun getComponent(): Component? {
		return null
	}

	override fun getValidator(): SerializablePredicate<in Any>? {
		return this.iValidator
	}

	override fun getErrorView(): ErrorView {
		return ErrorView(this.iFieldName to "input is invalid!")
	}

}