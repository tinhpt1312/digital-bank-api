package org.tinhpt.digital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);

    Optional<Account> findByAccountNumber(String accountNumber);

    // @Query("select a from Account a where a.id = :id and a.audit.deletedAt is
    // null")
    // Optional<Account> findById(@Param("id") Long id);
}
