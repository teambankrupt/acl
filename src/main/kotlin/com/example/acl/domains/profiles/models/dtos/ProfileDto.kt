package com.example.acl.domains.profiles.models.dtos

import com.example.acl.domains.profiles.models.enums.BloodGroup
import com.example.acl.domains.profiles.models.enums.MaritalStatus
import com.example.acl.domains.profiles.models.enums.Religion
import com.example.auth.enums.Genders
import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import java.time.Instant
import javax.validation.constraints.NotNull

class ProfileDto : BaseDto() {

    @NotNull
    @ApiModelProperty(required = true)
    var public: Boolean = false

    @NotNull
    @ApiModelProperty(required = true)
    lateinit var birthday: Instant

    var photo: String? = null

    @JsonProperty("user_photo_as_avatar")
    var userPhotoAsAvatar: Boolean = false

    @NotNull
    @ApiModelProperty(required = true)
    lateinit var gender: Genders

    @JsonProperty("blood_group")
    var bloodGroup: BloodGroup? = null

    @JsonProperty("marital_status")
    var maritalStatus: MaritalStatus? = null

    var religion: Religion? = null

    @NotNull
    @ApiModelProperty(required = true)
    @JsonProperty("user_id")
    var userId: Long = 0

    /*
    READONLY
     */

    @JsonProperty("username")
    @ApiModelProperty(readOnly = true)
    var username: String? = null

}
