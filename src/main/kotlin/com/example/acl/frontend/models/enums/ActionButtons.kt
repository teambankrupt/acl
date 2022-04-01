package com.example.acl.frontend.models.enums

enum class ActionButtons constructor(
	val label: String,
	val id: String,
) {
	SAVE("Save", "id_save"),
	CANCEL("Cancel", "id_cancel"),
	SUBMIT("Submit", "id_submit"),
	CLEAR("Clear", "id_clear");
}