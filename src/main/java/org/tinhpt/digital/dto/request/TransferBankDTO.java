package org.tinhpt.digital.dto.request;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferBankDTO {
    private Long sourceACcount;
    private Long targetAccount;
    private BigDecimal amount;
}
