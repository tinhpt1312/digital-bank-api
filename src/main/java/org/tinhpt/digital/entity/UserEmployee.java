package org.tinhpt.digital.entity;

import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;

@Entity
@Table(name = "user_employees")
@Getter 
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToOne
    @JoinColumn(name = "employee_id", unique = true) 
    private Employee employee;

    @Embedded
    @Builder.Default
    private Audit audit = new Audit();
}