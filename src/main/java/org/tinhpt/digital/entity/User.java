package org.tinhpt.digital.entity;


import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column()
    private boolean emailVerified;

    @Column
    private String provider;

    @Column
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_users_role_id"))
    private Role role;


    @Embedded
    @Builder.Default
    private Audit audit = new Audit();
}
