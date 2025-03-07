package org.tinhpt.digital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinhpt.digital.entity.common.Audit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "beneficiaries")
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "beneficiary_account_number", nullable = false)
    private String beneficiaryAccountNumber;

    @Column(name = "beneficiary_name", nullable = false)
    private String beneficiaryName;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Embedded
    @Builder.Default
    private Audit audit = new Audit();
}
