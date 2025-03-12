package org.tinhpt.digital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tinhpt.digital.entity.AccountRequest;

public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {
}
