package org.tinhpt.digital.dto.request;

import org.tinhpt.digital.type.CardType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardRequest {
    private Long accountId;
    private CardType cardType;
}
