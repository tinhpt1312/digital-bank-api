package org.tinhpt.digital.dto.request;


import lombok.Builder;
import lombok.Data;
import org.tinhpt.digital.type.TransactionType;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionRequest {
    private BigDecimal amount;
    private String currency;
    private String description;
    private TransactionType transactionType;
    private Long accountId;
    private Long destinationAccountId;
}
