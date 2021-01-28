package com.example.acl.domains.users.repositories;

import com.example.auth.entities.UrlAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlAccessRepository extends JpaRepository<UrlAccess, Long> {
    @Modifying
    @Query("DELETE FROM UrlAccess u WHERE u.privilege.id=:privilegeId")
    void deleteByPrivilegeId(@Param("privilegeId") Long privilegeId);
}
