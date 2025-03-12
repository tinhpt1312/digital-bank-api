package org.tinhpt.digital.dto.request;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBalanceAccountDTO {
    private BigDecimal amount;
}
