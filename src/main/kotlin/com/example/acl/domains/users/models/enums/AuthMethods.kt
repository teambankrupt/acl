package com.example.acl.domains.users.models.enums

enum class AuthMethods(
	val value: String
) {
	PHONE("phone"),
	EMAIL("email"),
	BOTH("both");

	companion object {
		fun fromValue(value: String): AuthMethods {
			return entries.first { it.value == value }
		}
	}
}