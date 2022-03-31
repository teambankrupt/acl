package com.example.acl.domains.users.models.enums

enum class AuthMethods constructor(
	val value: String
) {
	PHONE("phone"),
	EMAIL("email"),
	BOTH("both");

	companion object {
		fun fromValue(value: String): AuthMethods {
			return values().first { it.value == value }
		}
	}
}