package com.example.acl.domains.users.services

import com.example.acl.domains.home.models.CheckUsernameResponse
import com.example.acl.domains.users.models.entities.AcValidationToken
import com.example.auth.entities.User
import com.example.common.utils.DateUtil
import org.springframework.data.domain.Page
import java.time.Instant
import java.util.*

interface UserService {
    fun save(entity: User): User
    fun search(query: String, page: Int, size: Int): Page<User>
    fun find(id: Long): Optional<User>
    fun delete(id: Long, softDelete: Boolean)
    fun search(query: String?, role: String?, page: Int, size: Int): Page<User>
    fun findAll(page: Int): Page<User>
    fun findByRole(role: String, page: Int): Page<User>
    fun findByRole(role: String): List<User>

    fun register(token: String, user: User): User
    fun requireAccountValidationByOTP(phoneOrEmail: String, tokenValidUntil: Instant): AcValidationToken
    fun findByUsername(username: String): Optional<User>
    fun findByPhone(phone: String): Optional<User>
    fun findByEmail(email: String): Optional<User>

    fun changePassword(id: Long, currentPassword: String, newPassword: String): User
    fun setPassword(id: Long, newPassword: String): User
    fun handlePasswordResetRequest(username: String): AcValidationToken
    fun setRoles(id: Long, roleIds: List<Long>): User

    fun resetPassword(username: String, token: String, password: String): User

    fun toggleAccess(userId: Long, enable: Boolean)
    fun checkUsername(username: String): CheckUsernameResponse

    fun getGrowthStats(period: DateUtil.Periods): LinkedHashMap<String, Any>
}
