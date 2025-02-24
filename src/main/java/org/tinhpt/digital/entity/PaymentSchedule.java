package org.tinhpt.digital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.type.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment_schedules")
public class PaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    private LocalDateTime dueDate;

    private BigDecimal amountDue;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paymentDate;

    @Embedded
    @Builder.Default
    private Audit audit = new Audit();
}
