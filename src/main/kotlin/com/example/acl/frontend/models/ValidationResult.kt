package com.example.acl.frontend.models

class ValidationResult<V>(
	var valid: Boolean,
	var message: String,
	var value: V?
) {
}