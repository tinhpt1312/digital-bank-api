package org.tinhpt.digital.dto;


import lombok.Builder;
import lombok.Data;
import org.tinhpt.digital.type.TransactionStatus;
import org.tinhpt.digital.type.TransactionType;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String currency;
    private String description;
    private TransactionStatus status;
    private TransactionType transactionType;
    private Long accountId;
    private Long destinationAccountId;
}
