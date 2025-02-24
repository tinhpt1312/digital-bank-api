package org.tinhpt.digital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.type.BranchStatus;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "branches")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchId;

    @Column(nullable = false)
    private String name;

    private String address;

    private String phoneNumber;

    @Embedded
    @Builder.Default
    private Audit audit = new Audit();

    @Column(name = "branch_code", unique = true, nullable = false)
    private String branchCode;

    @Column(name = "email")
    private String email;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BranchStatus status;

    @OneToMany(mappedBy = "branch")
    private Set<Employee> employees = new HashSet<>();
}
