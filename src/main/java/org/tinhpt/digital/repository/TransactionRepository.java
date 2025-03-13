package org.tinhpt.digital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
