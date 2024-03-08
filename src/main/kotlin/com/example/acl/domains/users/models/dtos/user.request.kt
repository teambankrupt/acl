package com.example.acl.domains.users.models.dtos

import com.example.auth.entities.Role
import com.example.auth.entities.User
import com.example.auth.enums.Genders
import com.example.auth.utils.PasswordUtil
import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.annotations.NotNull
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserReq(
    @field:NotBlank(message = "Name is required!")
    @field:Size(min = 3, max = 50, message = "Name must be between 3 and 100 characters long!")
    val name: String,

    @field:NotBlank
    @field:Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters long!")
    val username: String,

    val phone: String?,

    @Email(message = "Invalid email!")
    val email: String?,

    @field:NotBlank
    @field:Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters long!")
    val password: String,

    @field:NotNull("Roles can't be empty!")
    @field:JsonProperty("role_names")
    val roleNames: Set<String>
) {
    fun asUser(getRoles: (roleNames: Set<String>) -> List<Role>): User =
        this.let { req ->
            User().apply {
                this.avatar =
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/User-avatar.svg/100px-User-avatar.svg.png"
                this.name = req.name
                this.username = req.username
                this.phone = req.phone
                this.email = req.email
                this.password = PasswordUtil.encryptPassword(req.password, PasswordUtil.EncType.BCRYPT_ENCODER, null)
                this.gender = Genders.NOT_SPECIFIED
                this.roles = getRoles(req.roleNames)
            }
        }
}