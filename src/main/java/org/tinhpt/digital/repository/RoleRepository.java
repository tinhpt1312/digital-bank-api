package org.tinhpt.digital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query("SELECT DISTINCT r FROM Role r " +
            "LEFT JOIN FETCH r.permissions p " +
            "WHERE r.audit.deletedAt IS NULL " +
            "AND (:search IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Role> findAllRoles(@Param("search") String search, Pageable pageable);

    @Modifying
    @Query("UPDATE Role r SET r.audit.deletedAt = :deletedAt, r.audit.deletedBy = :userId WHERE r.id IN :ids")
    void markRolesAsDeleted(List<Long> ids, LocalDateTime deletedAt, Long userId);
}
