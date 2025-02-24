package org.tinhpt.digital.entity;


import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.type.CustomerStatus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true, unique = true)
    private String governmentId;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    @Embedded
    @Builder.Default
    private Audit audit = new Audit();

    @Column(nullable = true)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "occupation")
    private String occupation;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Account> accounts = new HashSet<>();

    @Column(name = "kyc_verified")
    private boolean kycVerified;

}
