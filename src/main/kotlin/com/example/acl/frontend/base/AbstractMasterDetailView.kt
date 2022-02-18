package com.example.acl.frontend.base

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasStyle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import org.springframework.data.domain.Page
import java.lang.reflect.Field
import java.time.Instant
import java.util.*


abstract class AbstractMasterDetailView<T> : Div(), BeforeEnterObserver {
	private lateinit var klass: Class<T>
	private lateinit var items: Page<T>
	private lateinit var selectedObj: Optional<T>
	private var fields: List<Field> = listOf()

	fun initialize(klass: Class<T>, items: Page<T>, selectedObj: Optional<T>) {
		this.initialize(klass, items, selectedObj, null, null)
	}

	fun initialize(klass: Class<T>, items: Page<T>, selectedObj: Optional<T>, columnFields: Map<String, String>?) {
		this.initialize(klass, items, selectedObj, columnFields, null)
	}

	fun initialize(
		klass: Class<T>, items: Page<T>, selectedObj: Optional<T>,
		columnFields: Map<String, String>?, formFields: Map<String, String>?
	) {
		this.klass = klass
		this.items = items
		this.selectedObj = selectedObj

		addClassNames("flex", "flex-col", "h-full")

		val splitLayout = SplitLayout()
		splitLayout.setSizeFull()

		splitLayout.addToPrimary(this.createGridLayout(columnFields))
		splitLayout.addToSecondary(this.createEditorLayout(formFields))
		add(splitLayout)
	}

	fun createGridLayout(fieldsToShow: Map<String, String>?): Component {
		val div = Div()
		div.setId("grid-wrapper")
		div.setSizeFull()
		div.add(createGrid(fieldsToShow))
		return div
	}

	fun createGrid(columnFields: Map<String, String>?): Grid<T> {
		val showAllFields = columnFields.isNullOrEmpty()
		val grid: Grid<T> = Grid(klass, showAllFields)
		grid.setItems(this.items.content)
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
			.addValueChangeListener { event: ComponentValueChangeEvent<Grid<T>, T> ->
				if (event.value != null) {
					this.onRowSelected(event)
				} else {
					this.onRowUnselected(event)
				}
			}
		return grid
	}

	abstract fun onRowSelected(event: ComponentValueChangeEvent<Grid<T>, T>)

	abstract fun onRowUnselected(event: ComponentValueChangeEvent<Grid<T>, T>)

	abstract override fun beforeEnter(event: BeforeEnterEvent)

	fun createEditorLayout(formFields: Map<String, String>?): Div {
		val showAllFields = formFields.isNullOrEmpty()

		val editorLayoutDiv = Div()
		editorLayoutDiv.className = "flex flex-col space-s"
		editorLayoutDiv.width = "400px"
		val editorDiv = Div()
		editorDiv.className = "p-l flex-grow"
		editorLayoutDiv.add(editorDiv)
		val formLayout = FormLayout()

		this.fields = this.fields.ifEmpty { klass.declaredFields.map { it } }

		if (!showAllFields) {
			formFields!!.forEach {
				val field = this.fields.find { f -> f.name == it.key }
				if (field != null) {
					formLayout.add(this.createInput(field, it.value))
				}
			}
		} else {
			this.fields.forEach {
				formLayout.add(this.createInput(it, it.name))
			}
		}

		editorLayoutDiv.add(formLayout)
		editorLayoutDiv.add(this.createButtonLayout())
		return editorLayoutDiv
	}

	private fun createInput(field: Field, label: String): Component {
		if (field.type.isEnum) {
			val input = Select<String>()
			input.label = label
			val items = field.type.enumConstants.map { i -> i.toString() }
			input.setItems(items)
			input.setId(field.name)
			(input as HasStyle).addClassName("full-width")
			return input
		} else if (field.type == Instant::class.java) {
			val input = DatePicker(label)
			input.setId(field.name)
			(input as HasStyle).addClassName("full-width")
			return input
		} else if (field.type == Boolean::class.java) {
			val input = Checkbox(label)
			input.setId(field.name)
			(input as HasStyle).addClassName("full-width")
			return input
		}

		val input = TextField(label)
		input.setId(field.name)
		return input
	}

	private fun createButtonLayout(): HorizontalLayout {
		val buttonLayout = HorizontalLayout()
		buttonLayout.className = "w-full flex-wrap bg-contrast-5 py-s px-l"
		buttonLayout.isSpacing = true
		val cancel = Button("Cancel")
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
		val save = Button("Save")
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY)
		buttonLayout.add(save, cancel)
		return buttonLayout
	}

}