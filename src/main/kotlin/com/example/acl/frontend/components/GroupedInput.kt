package com.example.acl.frontend.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.data.provider.CallbackDataProvider
import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.selection.MultiSelectionEvent

class GroupedInput<L, V>() : CheckboxGroup<Pair<L, V>>(), AbstractInputV2<Set<Pair<L, V>>> {

	constructor(id: String, label: String) : this() {
		this.setId(id)
		this.label = label
	}

	fun withItems(values: List<Pair<L, V>>): GroupedInput<L, V> {
		this.setItems(values)
		return this
	}

	fun setDefaultValues(values: List<Pair<L, V>>): GroupedInput<L, V> {
		this.clear()
		this.select(values)
		return this
	}

	fun withDataProvider(
		fetchCallback: CallbackDataProvider.FetchCallback<Pair<L, V>, Void>,
		countCallback: CallbackDataProvider.CountCallback<Pair<L, V>, Void>
	): GroupedInput<L, V> {
		val provider = DataProvider.fromCallbacks(fetchCallback, countCallback)
		this.setItems(provider)
		this.setItemLabelGenerator { it.first.toString() }
		return this
	}

	fun withValueChangeListener(
		valueChangeListener: (e: ComponentValueChangeEvent<CheckboxGroup<Pair<L, V>>, Set<Pair<L, V>>>) -> Unit
	): GroupedInput<L, V> {
		this.addValueChangeListener {
			valueChangeListener(it)
		}
		return this
	}

	fun withSelectionListener(
		selectionListener: (e: MultiSelectionEvent<CheckboxGroup<Pair<L, V>>, Pair<L, V>>) -> Unit
	): GroupedInput<L, V> {
		this.addSelectionListener {
			selectionListener(it)
		}
		return this
	}

	override fun setVal(value: Set<Pair<L, V>>) {
		this.value = value
	}

	override fun getVal(): Set<Pair<L, V>>? {
		return this.value
	}

	override fun clearVal() {
		this.clear()
	}

	override fun getComponent(): Component {
		return this
	}


}