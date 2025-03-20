package org.tinhpt.digital.service;

import java.util.List;

import org.tinhpt.digital.dto.CardDTO;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.request.CreateCardRequest;
import org.tinhpt.digital.dto.request.QueryCardDTO;
import org.tinhpt.digital.dto.response.BankResponse;

public interface CardService {
    BankResponse createCard(CreateCardRequest cardRequest, Long userId);

    PagedResponse<CardDTO> getCardByQuery(QueryCardDTO queryCardDTO);

    List<CardDTO> getCardsByUserId(Long userId);

    CardDTO getCardById(Long cardId, Long userId);

    BankResponse blockCard(Long cardId, Long userId);

    BankResponse activateCard(Long cardId, Long userId);

    BankResponse deleteCard(Long cardId, Long userId);
}
