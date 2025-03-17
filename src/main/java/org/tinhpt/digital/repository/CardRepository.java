package org.tinhpt.digital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.Card;

@Repository
public interface CardRepository  extends JpaRepository<Card, Long>{
    
}
