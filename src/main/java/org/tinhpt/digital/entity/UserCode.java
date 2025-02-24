package org.tinhpt.digital.entity;


import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.utils.UserCodeType;

@Entity
@Table(name = "user_codes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private UserCodeType type;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;


    @Embedded
    private Audit audit;
}
