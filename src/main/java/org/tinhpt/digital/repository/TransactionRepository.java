package org.tinhpt.digital.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(" SELECT tr FROM Transaction tr " +
            " JOIN tr.account acc " +
            " WHERE tr.audit.deletedAt IS NULL " +
            " AND (:search IS null OR " +
            "     lower(tr.status) LIKE lower(concat('%', :search, '%'))) " +
            " OR (:search IS null OR " +
            "     lower(acc.accountNumber) LIKE lower(concat('%', :search, '%'))) " +
            " ORDER BY " +
            " CASE " +
            "   WHEN tr.status = 'PENDING' THEN 1 " +
            "   WHEN tr.status = 'FAILED' THEN 2 " +
            "   WHEN tr.status = 'COMPLETED' THEN 3 " +
            " END, tr.id DESC")
    Page<Transaction> findAllTransactions(@Param("search") String search, Pageable pageable);

    @Query("SELECT t FROM Transaction t JOIN t.account a WHERE a.user.id = :userId AND t.audit.deletedAt IS NULL ORDER BY t.id DESC")
    List<Transaction> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :id AND t.audit.createdAt BETWEEN :fromDate AND :toDate and t.audit.deletedAt IS NULL")
    List<Transaction> findTransactionsByAccountAndDateRange(@Param("id") Long id, @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate);

}
