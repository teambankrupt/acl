package com.example.acl.frontend.components.deprecated

import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.combobox.GeneratedVaadinComboBox.CustomValueSetEvent
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.function.SerializablePredicate

class AutoCompleteCombobox<B, V> : GenericValueInput<B> {
	private var component: ComboBox<V>

	constructor(
		fieldName: String,
		label: String,
		validator: SerializablePredicate<in Any>
	) : super(fieldName, label, validator) {
		this.iFieldName = fieldName
		this.iLabel = label
		this.iPlaceholder = label
		this.component = ComboBox(this.iLabel)
		this.iValidator = validator
		this.setUpAutocomplete()
	}

	constructor(
		fieldName: String,
		label: String,
		placeholder: String,
		defaultValue: String?,
		validator: SerializablePredicate<in Any>
	) : super(fieldName, label, placeholder, defaultValue, validator) {
		this.iFieldName = fieldName
		this.iLabel = label
		this.iPlaceholder = placeholder
		this.iDefaultValue = defaultValue
		this.iValidator = validator
		this.component = ComboBox(this.iLabel)
		this.setUpAutocomplete()
	}

	fun withItems(values: Map<String, V>): AutoCompleteCombobox<B, V> {
		this.component.setItems(values.values)
		return this
	}

	fun withDataProvider(provider: ListDataProvider<V>): AutoCompleteCombobox<B, V> {
		this.component.setItems(provider)
		return this
	}

	fun withListener(listener: AcListener<V>): AutoCompleteCombobox<B, V> {
		this.component.addValueChangeListener {
			listener.onAcChange(it)
		}
		this.component.addCustomValueSetListener {
			listener.onAcApplied(it)
		}
		return this
	}

	private fun setUpAutocomplete() {
		this.component.label = this.iLabel
		this.component.placeholder = this.iPlaceholder
	}

	fun setOptions(options: List<V>) {
		this.component.setItems(options)
	}

	override fun getComponent(): AbstractField<*, *> {
		return this.component
	}

	interface AcListener<V> {
		fun onAcChange(event: ComponentValueChangeEvent<ComboBox<V>, V>)
		fun onAcApplied(event: CustomValueSetEvent<ComboBox<V>>)
	}

}