package com.example.acl.frontend.components

import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.combobox.GeneratedVaadinComboBox.CustomValueSetEvent
import com.vaadin.flow.data.provider.ListDataProvider

class AutoCompleteCombobox<T> : AbstractInput {
	private var label: String
	private var placeholder: String
	private var fieldName: String
	private var defaultValue: String? = null
	private var component: ComboBox<T>

//	private var acListener: AcListener<T>? = null

	constructor(
		fieldName: String,
		label: String
	) {
		this.fieldName = fieldName
		this.label = label
		this.placeholder = label
		this.component = ComboBox(this.label)
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
		this.component = ComboBox(this.label)
		this.setUpAutocomplete()
	}

	fun withItems(values: Map<String,T>): AutoCompleteCombobox<T>{
		this.component.setItems(values.values)
		return this
	}

	fun withDataProvider(provider: ListDataProvider<T>): AutoCompleteCombobox<T>{
		this.component.setItems(provider)
		return this
	}

	fun withListener(listener: AcListener<T>): AutoCompleteCombobox<T>{
		this.component.addValueChangeListener {
			listener.onAcChange(it)
		}
		this.component.addCustomValueSetListener {
			listener.onAcApplied(it)
		}
		return this
	}

	private fun setUpAutocomplete() {
		this.component.label = this.label
		this.component.placeholder = this.placeholder
	}

	fun setOptions(options: List<T>) {
		this.component.setItems(options)
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

	override fun getComponent(): AbstractField<*, *> {
		return this.component
	}


	interface AcListener<T> {
		fun onAcChange(event: ComponentValueChangeEvent<ComboBox<T>, T>)
		fun onAcApplied(event: CustomValueSetEvent<ComboBox<T>>)
	}
}