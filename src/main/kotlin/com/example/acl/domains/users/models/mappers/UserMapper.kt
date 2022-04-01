package com.example.acl.domains.users.models.mappers

import com.example.acl.domains.users.models.dtos.UserRequest
import com.example.acl.domains.users.models.dtos.UserResponse
import com.example.acl.domains.users.models.dtos.UserSlice
import com.example.acl.domains.users.models.dtos.UserUpdateAdminDto
import com.example.acl.domains.users.models.enums.AuthMethods
import com.example.acl.domains.users.services.RoleService
import com.example.acl.domains.users.services.UserService
import com.example.auth.config.security.SecurityContext
import com.example.auth.entities.User
import com.example.auth.utils.PasswordUtil
import com.example.common.exceptions.exists.AlreadyExistsException
import com.example.common.exceptions.invalid.InvalidException
import com.example.common.exceptions.notfound.NotFoundException
import com.example.common.utils.ExceptionUtil
import com.example.common.utils.Validator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
@PropertySource("classpath:security.properties")
class UserMapper @Autowired constructor(
	private val userService: UserService, private val roleService: RoleService
) {
	@Value("\${auth.method}")
	lateinit var authMethod: String

	fun map(dto: UserRequest, exUser: User?): User {
		val user = exUser ?: User()
		user.name = dto.name
		user.gender = dto.gender
		user.phone = dto.phone
		user.username = dto.username
		user.avatar = dto.avatar
			?: "https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/User-avatar.svg/100px-User-avatar.svg.png"
		user.password = PasswordUtil.encryptPassword(dto.password, PasswordUtil.EncType.BCRYPT_ENCODER, null)
		user.email = dto.email
		val unrestrictedRole = this.roleService.findUnrestricted(dto.role)
			.orElseThrow { NotFoundException("Could not find role with name ${dto.role}") }
		user.roles = listOf(unrestrictedRole)
		this.validate(user)
		return user
	}

	fun map(user: User): UserResponse {
		val dto = UserResponse()
		dto.id = user.id
		dto.createdAt = user.createdAt
		dto.updatedAt = user.updatedAt

		dto.name = user.name
		dto.gender = user.gender
		dto.username = user.username
		dto.phone = user.phone
		dto.email = user.email
		dto.roles = user.roles.stream().map { role -> role.id }.collect(Collectors.toList())
		dto.avatar = user.avatar
		return dto
	}

	fun mapToSlice(user: User): UserSlice {
		val slice = UserSlice()
		return slice.apply {
			this.id = user.id
			this.name = user.name
			this.username = user.username
			this.avatar = user.avatar
		}
	}

	fun map(dto: UserUpdateAdminDto, exUser: User?): User {
		val user = exUser ?: User()
		user.apply {
			name = dto.name
			gender = dto.gender
			phone = dto.phone
			username = dto.username
			avatar = dto.avatar

			if (dto.password.isNotBlank()) {
				if (dto.password.length < 6) throw ExceptionUtil.forbidden("Invalid password length!")
				password = PasswordUtil.encryptPassword(dto.password, PasswordUtil.EncType.BCRYPT_ENCODER, null)
			} else if (exUser == null) // if password not entered for new user, throw exception
				throw ExceptionUtil.forbidden("Password length not be empty!")

			email = if (dto.email.isNullOrBlank()) null
			else {
				if (Validator.isValidEmail(dto.email!!)) dto.email
				else throw ExceptionUtil.invalid("Email ${dto.email} Invalid!")
			}

			if (exUser == null || !exUser.isAdmin) {
				roles = if (SecurityContext.getCurrentUser().isAdmin) roleService.findByIds(dto.roleIds)
				else roleService.findByIdsUnrestricted(dto.roleIds)
			}
			isEnabled = dto.enabled
			isAccountNonExpired = dto.accountNonExpired
			isAccountNonLocked = dto.accountNonLocked
			isCredentialsNonExpired = dto.credentialsNonExpired
		}

		this.validate(user)
		return user
	}

	fun mapToAdminDto(user: User): UserUpdateAdminDto {
		val dto = UserUpdateAdminDto()
		dto.id = user.id
		dto.createdAt = user.createdAt
		dto.updatedAt = user.updatedAt
		dto.password = user.password

		dto.avatar = user.avatar
		dto.name = user.name
		dto.username = user.username
		dto.phone = user.phone
		dto.email = user.email
		dto.gender = user.gender
		dto.roleIds = user.roles.map { it.id }
		dto.enabled = user.isEnabled
		dto.accountNonExpired = user.isAccountNonExpired
		dto.accountNonLocked = user.isAccountNonLocked
		dto.credentialsNonExpired = user.isCredentialsNonExpired
		return dto
	}


	fun validate(user: User) {
		val authMethod = AuthMethods.fromValue(this.authMethod)
		if (user.id == null) { // For new user
			if (this.userService.findByUsername(user.username).isPresent) throw AlreadyExistsException("User already exists with username: ${user.username}")
			if (authMethod == AuthMethods.PHONE) {
				if (user.phone == null || user.phone.isEmpty()) throw InvalidException("Phone number can't be null or empty!")
				if (this.userService.findByPhone(user.phone).isPresent) throw AlreadyExistsException("User already exists with phone: ${user.phone}")
			} else if (authMethod == AuthMethods.EMAIL) {
				if (user.email == null || user.email.isEmpty()) throw InvalidException("Email can't be null or empty!")
				if (this.userService.findByEmail(user.email).isPresent) throw AlreadyExistsException("User already exists with email: ${user.email}")
			} else { // both
				if ((user.phone == null || user.phone.isEmpty()) && (user.email == null || user.email.isEmpty())) throw InvalidException(
					"Email or phone can not be empty!"
				)
				if (user.phone.isNotEmpty())
					if (this.userService.findByPhone(user.phone).isPresent) throw AlreadyExistsException("User already exists with phone: ${user.phone}")
				if (user.email.isNotEmpty())
					if (this.userService.findByEmail(user.email).isPresent) throw AlreadyExistsException("User already exists with email: ${user.email}")
			}
		}
	}

}
