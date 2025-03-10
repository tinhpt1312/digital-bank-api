package org.tinhpt.digital.entity;


import jakarta.persistence.*;
import lombok.*;
import org.tinhpt.digital.entity.common.Audit;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "kyc_verifications")
public class KYC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "front_image")
    private String frontImage;

    @Column(name = "back_image")
    private String backImage;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Embedded
    @Builder.Default
    private Audit audit = new Audit();

}
