//package com.example.acl.frontend.components.inputs
//
//import com.example.acl.frontend.models.FieldValidator
//import com.vaadin.componentfactory.Autocomplete
//import com.vaadin.flow.component.Component
//
//class AutoCompleteTextFieldInput(
//	override var fieldValidator: FieldValidator<String>?
//) : Autocomplete(), AbstractInput<String> {
//
//	private var acListener: AcListener? = null
//
//	constructor(
//		id: String,
//		label: String,
//		validator: FieldValidator<String>?
//	) : this(validator) {
//		this.setId(id)
//		this.label = label
//		this.label = label
//		this.fieldValidator = validator
//	}
//
//	fun setListener(acListener: AcListener) {
//		this.acListener = acListener
//	}
//
//	interface AcListener {
//		fun onAcChange(event: AucompleteChangeEvent)
//		fun onAcApplied(event: AutocompleteValueAppliedEvent)
//		fun onAcCleared(event: ValueClearEvent)
//	}
//
//	override fun setVal(value: String) {
//		this.value = value
//	}
//
//	override fun getVal(): String? {
//		return this.value
//	}
//
//	override fun clearVal() {
//		this.value = ""
//	}
//
//	override fun getComponent(): Component {
//		return this
//	}
//
//	override fun getValidator(): FieldValidator<String>? {
//		return this.fieldValidator
//	}
//
//	override fun setMessage(error: Boolean, message: String?) {
////		this.resolveClass(this, error)
//		if (message != null) this.label = message
//		this.errorMessage = message
//	}
//}