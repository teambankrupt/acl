package com.example.acl.domains.profiles.models.dtos

import com.example.acl.domains.profiles.models.enums.BloodGroup
import com.example.acl.domains.profiles.models.enums.MaritalStatus
import com.example.acl.domains.profiles.models.enums.Religion
import com.example.auth.enums.Genders
import com.example.coreweb.domains.base.models.dtos.BaseDto
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import javax.validation.constraints.NotNull

class ProfileDto: BaseDto() {

    @NotNull
    lateinit var birthday: Instant

    var photo: String? = null

    @NotNull
    lateinit var gender: Genders

    @JsonProperty("blood_group")
    var bloodGroup: BloodGroup? = null

    @JsonProperty("marital_status")
    var maritalStatus: MaritalStatus? = null

    var religion: Religion? = null

    @NotNull
    @JsonProperty("user_id")
    var userId: Long = 0

}
