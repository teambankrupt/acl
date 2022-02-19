package com.example.acl.frontend.base

import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import org.springframework.data.domain.Page
import java.lang.reflect.Field
import java.util.*

abstract class AbstractBrowseView<T> : Div(), BeforeEnterObserver {
	private lateinit var klass: Class<T>
	private lateinit var data: Page<T>
	private var fields: List<Field> = listOf()
	private lateinit var selectedObj: Optional<T>
	private var listener: ItemSelectionListener<T>? = null

	fun initialize(
		klass: Class<T>, data: Page<T>, selectedObj: Optional<T>
	) {
		this.klass = klass
		this.data = data
		this.selectedObj = selectedObj

		this.createGridLayout(this.defineColumnFields())
	}

	abstract fun defineColumnFields(): Map<String, String>?

	fun createGridLayout(fieldsToShow: Map<String, String>?) {
		this.setId("grid-wrapper")
		this.setSizeFull()
		this.add(createGrid(fieldsToShow))
	}

	fun createGrid(columnFields: Map<String, String>?): Grid<T> {
		val showAllFields = columnFields.isNullOrEmpty()
		val grid: Grid<T> = Grid(klass, showAllFields)
		grid.setItems(this.data.content)
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER)
		grid.setHeightFull()
		if (!showAllFields) {
			this.fields = this.fields.ifEmpty { klass.declaredFields.map { it } }
			val fieldNames = fields.map { it.name }
			columnFields!!.forEach {
				if (fieldNames.contains(it.key)) {
					grid.addColumn(it.key)
						.setHeader(it.value)
						.isAutoWidth = true
				}
			}
		}
		// when a row is selected or deselected, populate form

		// when a row is selected or deselected, populate form
		grid.asSingleSelect()
			.addValueChangeListener { event: AbstractField.ComponentValueChangeEvent<Grid<T>, T> ->
				if (event.value != null) {
					this.onRowSelected(event)
				} else {
					this.onRowUnselected(event)
				}
			}
		return grid
	}

	abstract override fun beforeEnter(event: BeforeEnterEvent)

	fun onRowSelected(event: AbstractField.ComponentValueChangeEvent<Grid<T>, T>) {
			val item = event.value
			this.listener?.onItemSelected(true, item)
	}

	fun onRowUnselected(event: AbstractField.ComponentValueChangeEvent<Grid<T>, T>) {
			val item = event.value
			this.listener?.onItemSelected(false, item)
	}

	fun setItemSelectionListener(listener: ItemSelectionListener<T>){
		this.listener = listener
	}

	abstract fun onItemPersisted(item: T?)

	interface ItemSelectionListener<T> {
		fun onItemSelected(selected: Boolean, item: T?)
	}
}