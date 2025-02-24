package org.tinhpt.digital.entity;


import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.type.UserCodeType;

import java.util.Date;

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
    @Enumerated(EnumType.STRING)
    private UserCodeType type;

    @Column(nullable = true)
    private Date usedAt;

    @Column(nullable = false)
    private Date expiredAt;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;


    @Embedded
    @Builder.Default
    private Audit audit = new Audit();
}
