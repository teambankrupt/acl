package com.example.acl.domains.users.models.dtos

import com.example.auth.enums.Genders
import com.example.coreweb.domains.base.models.dtos.BaseDto
import javax.validation.constraints.*

class UserUpdateAdminDto : BaseDto() {
    @NotBlank
    lateinit var name: String

    @NotBlank
    @Size(min = 3)
    lateinit var username: String

    var phone: String? = null

    var timeZone: String? = null

    @Email
    var email: String? = null

    lateinit var password: String

    @NotNull
    lateinit var gender: Genders

    @NotEmpty
    lateinit var roleIds: List<Long>

    var enabled = false

    var accountNonExpired = false

    var accountNonLocked = false

    var credentialsNonExpired = false

    @Size(max = 511)
    var avatar: String? = null
}
