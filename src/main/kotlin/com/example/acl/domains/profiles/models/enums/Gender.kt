package com.example.acl.domains.profiles.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 7/3/21
 */
enum class Gender(
    @JsonProperty("label") var label: String
) {

    MALE( "Male"),
    FEMALE( "Female"),
    OTHER( "Other");

}
