package com.example.acl.frontend.components

import com.example.acl.frontend.views.ErrorView
import com.vaadin.flow.component.AbstractField
import com.vaadin.flow.component.Component
import com.vaadin.flow.function.SerializablePredicate

interface AbstractInput<T> {
	fun getLabel(): String
	fun getPlaceholder(): String
	fun getFieldName(): String
	fun getDefaultValue(): String?
	fun getComponent(): Component?
	fun getValidator(): SerializablePredicate<T>?
	fun getErrorView(): ErrorView
}