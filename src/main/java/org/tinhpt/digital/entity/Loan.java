package org.tinhpt.digital.entity;


import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.utils.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private BigDecimal interestRate;

    private int termMonths;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Embedded
    private Audit audit;

    @Column(name = "loan_number", unique = true)
    private String loanNumber;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "disbursement_date")
    private LocalDateTime disbursementDate;

    @Column(name = "total_amount_paid")
    private BigDecimal totalAmountPaid;

    @Column(name = "remaining_amount")
    private BigDecimal remainingAmount;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;
}
