package com.example.acl.frontend.base

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.GridVariant
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.splitlayout.SplitLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import org.springframework.data.domain.Page
import java.util.*


abstract class AbstractMasterDetailView<T> : Div(), BeforeEnterObserver {
	private lateinit var klass: Class<T>
	private lateinit var items: Page<T>
	private lateinit var selectedObj: Optional<T>

	fun initialize(klass: Class<T>, items: Page<T>, selectedObj: Optional<T>) {
		this.initialize(klass, items, selectedObj, null)
	}

	fun initialize(
		klass: Class<T>, items: Page<T>, selectedObj: Optional<T>, fieldsToShow: Map<String, String>?
	) {
		this.klass = klass
		this.items = items
		this.selectedObj = selectedObj

		addClassNames("flex", "flex-col", "h-full")

		val splitLayout = SplitLayout()
		splitLayout.setSizeFull()

		splitLayout.addToPrimary(this.createGridLayout(fieldsToShow))
		splitLayout.addToSecondary(Div())
		add(splitLayout)
	}

	fun createGridLayout(fieldsToShow: Map<String, String>?): Component {
		val div = Div()
		div.setId("grid-wrapper")
		div.setSizeFull()
		div.add(createGrid(fieldsToShow))
		return div
	}

	fun createGrid(fieldsToShow: Map<String, String>?): Grid<T> {
		val showAllFields = fieldsToShow.isNullOrEmpty()
		val grid: Grid<T> = Grid(klass, showAllFields)
		grid.setItems(this.items.content)
		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER)
		grid.setHeightFull()
		if (!showAllFields) {
			val fields = klass.declaredFields.map { it.name }
			fieldsToShow!!.forEach {
				if (fields.contains(it.key))
					grid.addColumn(it.key).setHeader(it.value)
			}
		}
		return grid
	}

	abstract override fun beforeEnter(event: BeforeEnterEvent)

}