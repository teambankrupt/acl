package com.example.acl.domains.users.repositories

import org.springframework.data.jpa.repository.JpaRepository
import com.example.auth.entities.UrlAccess
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UrlAccessRepository : JpaRepository<UrlAccess, Long> {
    @Modifying
    @Query("DELETE FROM UrlAccess u WHERE u.privilege.id=:privilegeId")
    fun deleteByPrivilegeId(@Param("privilegeId") privilegeId: Long)

    @Query("SELECT u FROM UrlAccess u WHERE u.privilege.id=:privilegeId")
    fun findByPrivilege(@Param("privilegeId") privilegeId: Long): List<UrlAccess>
}