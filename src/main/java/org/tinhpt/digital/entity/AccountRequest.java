package org.tinhpt.digital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinhpt.digital.type.RequestStatus;

import java.util.Date;

@Entity
@Table(name = "account_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @Column
    private String requestType;

    @Enumerated(EnumType.STRING)
    @Column
    private RequestStatus status;

    @Column
    private String details;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true)
    private User approvedBy;

    @Builder.Default
    private Date createdAt = new Date();

    private Date approvedAt;
}



