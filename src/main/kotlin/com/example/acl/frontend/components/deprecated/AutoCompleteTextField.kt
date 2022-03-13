package com.example.acl.frontend.components.deprecated

import com.vaadin.componentfactory.Autocomplete
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.function.SerializablePredicate

class AutoCompleteTextField<B> : GenericValueInput<B> {

	private var component: Autocomplete

	private var acListener: AcListener? = null

	constructor(
		fieldName: String,
		label: String,
		validator: SerializablePredicate<in Any>?
	) : super(fieldName, label, validator) {
		this.component = Autocomplete(5)
		this.setUpAutocomplete()
	}

	constructor(
		fieldName: String,
		label: String,
		placeholder: String,
		defaultValue: String?,
		validator: SerializablePredicate<in Any>?
	) : super(fieldName, label, placeholder, defaultValue, validator) {
		this.component = Autocomplete(5)
		this.setUpAutocomplete()
	}

	fun setListener(acListener: AcListener) {
		this.acListener = acListener
	}

	private fun setUpAutocomplete() {
		this.component.label = this.iLabel
		this.component.setPlaceholder(this.iPlaceholder)
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


	override fun getComponent(): AbstractField<*, *>? {
		return null
	}


	interface AcListener {
		fun onAcChange(event: Autocomplete.AucompleteChangeEvent)
		fun onAcApplied(event: Autocomplete.AutocompleteValueAppliedEvent)
		fun onAcCleared(event: Autocomplete.ValueClearEvent)
	}
}