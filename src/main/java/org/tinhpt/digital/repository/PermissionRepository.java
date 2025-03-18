package org.tinhpt.digital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.Permission;
import org.tinhpt.digital.type.SubjectName;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query("SELECT p FROM Permission p WHERE p.subject = :subjectName and p.audit.deletedAt is null")
    List<Permission> findBySubjectName(@Param("subjectName") SubjectName subjectName);

    @Query("SELECT p FROM Permission p WHERE p.id = :id and p.audit.deletedAt is null")
    Optional<Permission> findById(@Param("id") Long id);

    @Query("SELECT p FROM Permission p WHERE p.audit.deletedAt is null")
    List<Permission> findAll();

    @Query("SELECT p FROM Permission p WHERE p.id = :id and p.audit.deletedAt is null")
    Optional<Permission> findAllById(@Param("id") Long id);
}
