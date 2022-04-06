package com.example.acl.domains.users.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.Instant
import java.util.Date
import javax.validation.constraints.NotBlank

@ApiModel(description = "To create a privilege with permission to access certain urls")
class PrivilegeDto : BaseDto() {

	@NotBlank
	@ApiModelProperty(name = "A Unique string without space", example = "ACCESS_USER_DATA")
	lateinit var name: String

	var description: String? = null

	@NotBlank
	@ApiModelProperty(name = "A readable label for the Privilege", example = "Read User Data")
	lateinit var label: String

	/*
	WRITE ONLY
	 */
	@NotBlank.List
	@JsonProperty("urls_all_access")
	@ApiModelProperty(dataType = "List")
	var urlsAllAccess: List<String> = ArrayList()

	@NotBlank.List
	@JsonProperty("urls_read_access")
	@ApiModelProperty(dataType = "List")
	var urlsReadAccess: List<String> = ArrayList()

	@NotBlank.List
	@JsonProperty("urls_create_access")
	@ApiModelProperty(dataType = "List")
	var urlsCreateAccess: List<String> = ArrayList()

	@NotBlank.List
	@JsonProperty("urls_update_access")
	@ApiModelProperty(dataType = "List")
	var urlsUpdateAccess: List<String> = ArrayList()

	@NotBlank.List
	@JsonProperty("urls_delete_access")
	@ApiModelProperty(dataType = "List")
	var urlsDeleteAccess: List<String> = ArrayList()

	/*
	READ ONLY
	 */

	@JsonIgnore
	var lastUpdated: Instant? = null


}
