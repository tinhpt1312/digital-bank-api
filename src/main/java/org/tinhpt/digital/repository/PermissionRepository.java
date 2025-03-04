package org.tinhpt.digital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.Permission;

import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
