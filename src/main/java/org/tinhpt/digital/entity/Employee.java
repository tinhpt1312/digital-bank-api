package org.tinhpt.digital.entity;


import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.type.EmployeeStatus;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Embedded
    @Builder.Default
    private Audit audit = new Audit();

    @Column(name = "employee_code", unique = true, nullable = false)
    private String employeeCode;

    @OneToOne(mappedBy = "employee")
    private UserEmployee userEmployee;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
