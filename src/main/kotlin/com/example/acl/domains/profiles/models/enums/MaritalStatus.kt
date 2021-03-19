package com.example.acl.domains.profiles.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 7/3/21
 */
enum class MaritalStatus(
    @JsonProperty("label") var label: String
) {

    MARRIED("Married"),
    UNMARRIED("Unmarried"),
    DIVORCED("Divorced"),
    SEPARATED("Separated"),
    WIDOWER("Widower"),
    WIDOW("Widow"),
    OTHERS("Others");

}
