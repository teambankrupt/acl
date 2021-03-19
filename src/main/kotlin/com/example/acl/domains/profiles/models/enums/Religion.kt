package com.example.acl.domains.profiles.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @project IntelliJ IDEA
 * @author mir00r on 7/3/21
 */
enum class Religion(
    @JsonProperty("name_en") var label: String
) {

    ISLAM("Islam"),
    HINDU("Hinduism"),
    BUDDHIST("Buddha"),
    CHRISTIAN("Christian"),
    HUMANITY("Humanity"),
    OTHER("Others");

}
