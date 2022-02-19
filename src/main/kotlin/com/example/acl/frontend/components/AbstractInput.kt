package com.example.acl.frontend.components

import com.vaadin.flow.component.AbstractField

abstract class AbstractInput {
	abstract fun getLabel(): String
	abstract fun getPlaceholder(): String
	abstract fun getFieldName(): String
	abstract fun getDefaultValue(): String?
	abstract fun getComponent(): AbstractField<*, *>?
}