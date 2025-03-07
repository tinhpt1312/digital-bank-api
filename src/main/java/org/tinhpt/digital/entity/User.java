package org.tinhpt.digital.entity;


import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.tinhpt.digital.entity.common.Audit;

import java.io.Serializable;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Beneficiary> beneficiaries;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications;


    @Embedded
    @Builder.Default
    private Audit audit = new Audit();
}
