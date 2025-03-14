package org.tinhpt.digital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.AccountRequest;


@Repository
public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {

    @Query("SELECT ar FROM AccountRequest ar order by " +
            "CASE " +
            "WHEN ar.status = 'PENDING' THEN 1" +
            "WHEN ar.status = 'REJECTED' THEN 2" +
            "WHEN ar.status = 'APPROVED' THEN 3" +
            " end , ar.id DESC ")
    Page<AccountRequest> findAllRequests(Pageable pageable);

}
