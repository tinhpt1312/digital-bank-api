package org.tinhpt.digital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.Card;

import java.util.List;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByCardNumber(String cardNumber);

    @Query("SELECT c FROM Card c " +
            "WHERE c.account.user.id = :userId " +
            "AND c.audit.deletedAt IS NULL " +
            "ORDER BY c.id DESC")
    List<Card> findByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Card c " +
            "WHERE c.audit.deletedAt IS NULL " +
            "AND (:search IS NULL " +
            "OR CAST(c.account.id AS string) LIKE CONCAT('%', :search, '%') " +
            "OR LOWER(c.cardNumber) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Card> findAllCards(@Param("search") String search, Pageable pageable);
}
