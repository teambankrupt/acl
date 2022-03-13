package com.example.acl.frontend.components

import com.example.acl.frontend.models.FieldValidator
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.datetimepicker.DateTimePicker
import java.time.LocalDateTime

class DateTimeInput(
	override var fieldValidator: FieldValidator<LocalDateTime>?
) : DateTimePicker(), AbstractInputV2<LocalDateTime> {

	constructor(id: String, label: String, fieldValidator: FieldValidator<LocalDateTime>?) : this(fieldValidator) {
		this.setId(id)
		this.label = label
		this.datePlaceholder = label
	}

	override fun setVal(value: LocalDateTime) {
		this.value = value
	}

	override fun getVal(): LocalDateTime? {
		return this.value
	}

	override fun getComponent(): Component {
		return this
	}

	override fun clearVal() {
		this.clear()
	}

	override fun getValidator(): FieldValidator<LocalDateTime>? {
		return this.fieldValidator
	}

	override fun setErrMessage(errorMsg: String) {
		this.errorMessage = errorMsg
		this.label = this.label + "\n($errorMsg)"
	}

}