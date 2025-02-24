package org.tinhpt.digital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.UserCode;

import java.util.Optional;


@Repository
public interface UserCodeRepository extends JpaRepository<UserCode, Long> {
    Optional<UserCode> findByUser_EmailAndCode(String email, String code);
}
