package org.tinhpt.digital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.type.AccountStatus;
import org.tinhpt.digital.type.AccountType;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Embedded
    @Builder.Default
    private Audit audit = new Audit();

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(nullable = false)
    private String currency;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "minimum_balance")
    private BigDecimal minimumBalance;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;
}
