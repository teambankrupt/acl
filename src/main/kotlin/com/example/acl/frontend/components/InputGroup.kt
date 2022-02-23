package com.example.acl.frontend.components

import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.data.provider.CallbackDataProvider
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.selection.MultiSelectionEvent

class InputGroup<L, V> : AbstractInput {
	private var label: String
	private var placeholder: String
	private var fieldName: String
	private var defaultValues: List<V> = mutableListOf()
	private var component: CheckboxGroup<Pair<L, V>>

	constructor(
		fieldName: String,
		label: String
	) {
		this.fieldName = fieldName
		this.label = label
		this.placeholder = label
		this.component = CheckboxGroup<Pair<L, V>>()
		this.component.label = this.label
	}

	constructor(
		fieldName: String,
		label: String,
		placeholder: String
	) {
		this.fieldName = fieldName
		this.label = label
		this.placeholder = placeholder
		this.component = CheckboxGroup<Pair<L, V>>()
		this.component.label = this.label
	}

	fun withItems(values: List<Pair<L, V>>): InputGroup<L, V> {
		this.component.setItems(values)
		return this
	}

	fun setDefaultValues(values: List<Pair<L, V>>): InputGroup<L, V> {
		this.component.clear()
		this.component.select(values)
		return this
	}

	fun withDataProvider(
		fetchCallback: CallbackDataProvider.FetchCallback<Pair<L, V>, Void>,
		countCallback: CallbackDataProvider.CountCallback<Pair<L, V>, Void>
	): InputGroup<L, V> {
		val provider = DataProvider.fromCallbacks(fetchCallback, countCallback)
		this.component.setItems(provider)
		this.component.setItemLabelGenerator { it.first.toString() }
		return this
	}

	fun withValueChangeListener(
		valueChangeListener: (e: AbstractField.ComponentValueChangeEvent<CheckboxGroup<Pair<L, V>>, Set<Pair<L, V>>>) -> Unit
	): InputGroup<L, V> {
		this.component.addValueChangeListener {
			valueChangeListener(it)
		}
		return this
	}

	fun withSelectionListener(
		selectionListener: (e: MultiSelectionEvent<CheckboxGroup<Pair<L, V>>, Pair<L, V>>) -> Unit
	): InputGroup<L, V> {
		this.component.addSelectionListener {
			selectionListener(it)
		}
		return this
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
		return null
	}

	override fun getComponent(): AbstractField<*, *> {
		return this.component
	}

}