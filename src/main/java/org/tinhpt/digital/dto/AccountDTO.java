package org.tinhpt.digital.dto;

import lombok.*;
import org.tinhpt.digital.type.AccountStatus;
import org.tinhpt.digital.type.AccountType;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AccountDTO {
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
    private Long userId;
}
