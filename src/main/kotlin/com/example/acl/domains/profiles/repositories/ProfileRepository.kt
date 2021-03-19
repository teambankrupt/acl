package com.example.acl.domains.profiles.repositories

import com.example.acl.domains.profiles.models.enums.BloodGroup
import com.example.acl.domains.profiles.models.enums.Gender
import com.example.acl.domains.profiles.models.enums.Religion
import com.example.acl.domains.profiles.models.entities.Profile
import com.example.acl.domains.profiles.models.enums.MaritalStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProfileRepository : JpaRepository<Profile, Long> {

    @Query("SELECT e FROM Profile e WHERE (:q IS NULL OR LOWER(e.createdBy) LIKE %:q%) AND e.deleted=FALSE")
    fun search(@Param("q") query: String?, pageable: Pageable): Page<Profile>

    @Query("SELECT p FROM Profile p WHERE (:q IS NULL OR LOWER(p.createdBy) LIKE %:q%) AND (:gender IS NULL OR p.gender =:gender) AND (:bloodGroup IS NULL OR p.bloodGroup =:bloodGroup) AND (:maritalStatus IS NULL OR p.maritalStatus =:maritalStatus) AND (:religion IS NULL OR p.religion =:religion) AND (:userId IS NULL OR p.user.id =:userId) AND (:username IS NULL OR p.user.username =:username)  AND p.deleted=FALSE")
    fun search(
        @Param("q") query: String?,
        @Param("gender") gender: Gender?,
        @Param("bloodGroup") bloodGroup: BloodGroup?,
        @Param("maritalStatus") maritalStatus: MaritalStatus?,
        @Param("religion") religion: Religion?,
        @Param("userId") userId: Long?,
        @Param("username") username: String?,
        pageable: Pageable
    ): Page<Profile>

    @Query("SELECT p FROM Profile p WHERE p.id=:id AND p.deleted=FALSE")
    fun find(@Param("id") id: Long): Optional<Profile>

    @Query("SELECT p FROM Profile p WHERE p.user.id=:userId AND p.deleted=FALSE")
    fun findByUserId(@Param("userId") userId: Long): Optional<Profile>

    @Query("SELECT p FROM Profile p WHERE p.user.username=:username AND p.deleted=FALSE")
    fun findByUserName(@Param("username") username: String): Optional<Profile>

}
