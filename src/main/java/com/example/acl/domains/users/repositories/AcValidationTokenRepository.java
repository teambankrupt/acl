package com.example.acl.domains.users.repositories;


import com.example.acl.domains.users.models.entities.AcValidationToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;

@Repository
public interface AcValidationTokenRepository extends JpaRepository<AcValidationToken, Long> {

    @Query("""
            SELECT t FROM AcValidationToken t
            WHERE (t.createdAt BETWEEN :fromDate AND :toDate)
            AND (:q IS NULL OR t.username LIKE %:q% OR t.username LIKE %:q%)
             AND t.deleted=false
            """)
    Page<AcValidationToken> search(
            @Param("fromDate") Instant fromDate,
            @Param("toDate") Instant toDate,
            @Param("q") String query,
            Pageable pageable
    );

    AcValidationToken findFirstByTokenOrderByIdDesc(String token);

    AcValidationToken findFirstByUsernameOrderByIdDesc(String phone);

    @Query("SELECT COUNT(t) FROM AcValidationToken t WHERE t.id=:id AND (t.createdAt BETWEEN :fromDate AND :toDate) AND t.deleted=false")
    int count(@Param("id") Long id, @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);
}
