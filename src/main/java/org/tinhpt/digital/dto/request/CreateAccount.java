package org.tinhpt.digital.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;
import org.tinhpt.digital.type.AccountType;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CreateAccount {

    @Parameter(example = "SAVINGS")
    private AccountType accountType;

    private String currency;

}
