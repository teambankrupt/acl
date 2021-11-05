package com.example.acl.domains.home.models

import com.fasterxml.jackson.annotation.JsonProperty

class CheckUsernameResponse() {

    constructor(available: Boolean, reason: String) : this() {
        this.available = available
        this.reason = reason
    }

    @JsonProperty("available")
    var available: Boolean = false

    @JsonProperty("reason")
    lateinit var reason: String
}