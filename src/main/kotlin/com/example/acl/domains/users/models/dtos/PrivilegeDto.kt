package com.example.acl.domains.users.models.dtos

import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
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

    @NotBlank.List
    @JsonProperty("urls_all_access")
    @ApiModelProperty(dataType = "List")
    lateinit var urlsAllAccess: List<String>

    @NotBlank.List
    @JsonProperty("urls_read_access")
    @ApiModelProperty(dataType = "List")
    lateinit var urlsReadAccess: List<String>

    @NotBlank.List
    @JsonProperty("urls_create_access")
    @ApiModelProperty(dataType = "List")
    lateinit var urlsCreateAccess: List<String>

    @NotBlank.List
    @JsonProperty("urls_update_access")
    @ApiModelProperty(dataType = "List")
    lateinit var urlsUpdateAccess: List<String>

    @NotBlank.List
    @JsonProperty("urls_delete_access")
    @ApiModelProperty(dataType = "List")
    lateinit var urlsDeleteAccess: List<String>

}
