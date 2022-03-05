package com.example.acl.frontend.components

import com.example.acl.frontend.views.ErrorView
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.data.provider.CallbackDataProvider
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.selection.MultiSelectionEvent
import com.vaadin.flow.function.SerializablePredicate

class InputGroup<B, L, V> : AbstractInput<B> {
	private var label: String
	private var placeholder: String
	private var fieldName: String
	private var defaultValues: List<V> = mutableListOf()
	private var component: CheckboxGroup<Pair<L, V>>
	private var iValidator: SerializablePredicate<B>? = null

	constructor(
		fieldName: String,
		label: String,
		validator: SerializablePredicate<B>
	) {
		this.fieldName = fieldName
		this.label = label
		this.placeholder = label
		this.component = CheckboxGroup<Pair<L, V>>()
		this.component.label = this.label
		this.iValidator = validator
	}

	constructor(
		fieldName: String,
		label: String,
		placeholder: String,
		validator: SerializablePredicate<B>
	) {
		this.fieldName = fieldName
		this.label = label
		this.placeholder = placeholder
		this.component = CheckboxGroup<Pair<L, V>>()
		this.component.label = this.label
		this.iValidator = validator
	}

	fun withItems(values: List<Pair<L, V>>): InputGroup<B, L, V> {
		this.component.setItems(values)
		return this
	}

	fun setDefaultValues(values: List<Pair<L, V>>): InputGroup<B, L, V> {
		this.component.clear()
		this.component.select(values)
		return this
	}

	fun withDataProvider(
		fetchCallback: CallbackDataProvider.FetchCallback<Pair<L, V>, Void>,
		countCallback: CallbackDataProvider.CountCallback<Pair<L, V>, Void>
	): InputGroup<B, L, V> {
		val provider = DataProvider.fromCallbacks(fetchCallback, countCallback)
		this.component.setItems(provider)
		this.component.setItemLabelGenerator { it.first.toString() }
		return this
	}

	fun withValueChangeListener(
		valueChangeListener: (e: AbstractField.ComponentValueChangeEvent<CheckboxGroup<Pair<L, V>>, Set<Pair<L, V>>>) -> Unit
	): InputGroup<B, L, V> {
		this.component.addValueChangeListener {
			valueChangeListener(it)
		}
		return this
	}

	fun withSelectionListener(
		selectionListener: (e: MultiSelectionEvent<CheckboxGroup<Pair<L, V>>, Pair<L, V>>) -> Unit
	): InputGroup<B, L, V> {
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

	override fun getValidator(): SerializablePredicate<B>? {
		return this.iValidator
	}

	override fun getErrorView(): ErrorView {
		return ErrorView(this.fieldName to "input is invalid.")
	}

}