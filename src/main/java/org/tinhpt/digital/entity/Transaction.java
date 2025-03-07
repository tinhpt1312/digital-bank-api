package org.tinhpt.digital.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinhpt.digital.entity.common.Audit;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "description")
    private String description;

    @Embedded
    @Builder.Default
    private Audit audit = new Audit();
}
