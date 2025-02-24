package org.tinhpt.digital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tinhpt.digital.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
