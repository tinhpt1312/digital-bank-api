package org.tinhpt.digital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.AccountRequest;


@Repository
public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {
}
