package com.example.acl.frontend.components

import com.vaadin.componentfactory.Autocomplete
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.Component

class AutoCompleteTextField : AbstractInput {
	private var label: String
	private var placeholder: String
	private var fieldName: String
	private var defaultValue: String? = null
	private var component: Autocomplete

	private var acListener: AcListener? = null

	constructor(
		fieldName: String,
		label: String
	) {
		this.fieldName = fieldName
		this.label = label
		this.placeholder = label
		this.component = Autocomplete(5)
		this.setUpAutocomplete()
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
		this.component = Autocomplete(5)
		this.setUpAutocomplete()
	}

	fun setListener(acListener: AcListener) {
		this.acListener = acListener
	}

	private fun setUpAutocomplete() {
		this.component.label = this.label
		this.component.setPlaceholder(this.placeholder)
		this.component.addChangeListener {
			if (this.acListener != null)
				this.acListener!!.onAcChange(it)
		}
		this.component.addAutocompleteValueAppliedListener {
			if (this.acListener != null)
				this.acListener!!.onAcApplied(it)
		}
		this.component.addValueClearListener {
			if (this.acListener != null)
				this.acListener!!.onAcCleared(it)
		}
	}

	fun setOptions(options: List<String>) {
		this.component.options = options
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

	override fun getComponent(): AbstractField<*, *>? {
		return null
	}


	interface AcListener {
		fun onAcChange(event: Autocomplete.AucompleteChangeEvent)
		fun onAcApplied(event: Autocomplete.AutocompleteValueAppliedEvent)
		fun onAcCleared(event: Autocomplete.ValueClearEvent)
	}
}