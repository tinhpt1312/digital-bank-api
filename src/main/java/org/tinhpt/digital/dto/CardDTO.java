package org.tinhpt.digital.dto;

import java.time.LocalDate;

import org.tinhpt.digital.type.CardStatus;
import org.tinhpt.digital.type.CardType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardDTO {
    private Long id;
    private Long accountId;
    private String cardNumber;
    private String accountNumber;
    private CardType cardType;
    private LocalDate expiryDate;
    private String cvv;
    private CardStatus status;
}