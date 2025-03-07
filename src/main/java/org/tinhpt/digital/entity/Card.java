package org.tinhpt.digital.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinhpt.digital.entity.common.Audit;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;

    @Column(name = "card_type", nullable = false)
    private String cardType;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "cvv", nullable = false)
    private String cvv;

    @Column(name = "status", nullable = false)
    private String status;

    @Embedded
    @Builder.Default
    private Audit audit = new Audit();
}
