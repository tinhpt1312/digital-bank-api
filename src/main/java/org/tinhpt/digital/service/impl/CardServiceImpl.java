package org.tinhpt.digital.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.CardDTO;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.request.CreateCardRequest;
import org.tinhpt.digital.dto.request.QueryCardDTO;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.Card;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.repository.AccountRepository;
import org.tinhpt.digital.repository.CardRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.CardService;
import org.tinhpt.digital.type.CardStatus;
import org.tinhpt.digital.type.CardType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    private CardDTO convertToDTO(Card card) {
        return CardDTO.builder()
                .accountId(card.getId())
                .accountNumber(card.getCardNumber())
                .cardNumber(maskCardNumber(card.getCardNumber()))
                .cardType(CardType.valueOf(card.getCardType()))
                .expiryDate(card.getExpiryDate())
                .cvv(maskCVV())
                .status(CardStatus.valueOf(card.getStatus()))
                .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account not found with id: " + id));
    }

    private CardDTO convertToDTOWithFullDetails(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .accountId(card.getAccount().getId())
                .accountNumber(card.getAccount().getAccountNumber())
                .cardNumber(card.getCardNumber())
                .cardType(CardType.valueOf(card.getCardType()))
                .expiryDate(card.getExpiryDate())
                .cvv(card.getCvv())
                .status(CardStatus.valueOf(card.getStatus()))
                .build();
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("4");

        for (int i = 0; i < 14; i++) {
            sb.append(random.nextInt(10));
        }

        int sum = 0;
        boolean alternate = false;
        for (int i = sb.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(sb.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        sb.append(checkDigit);

        String cardNumber = sb.toString();

        if (cardRepository.existsByCardNumber(cardNumber)) {
            return generateCardNumber();
        }

        return cardNumber;
    }

    private String generateCVV() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    private LocalDate generateExpiryDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = currentDate.plusYears(5);
        return expiryDate;
    }

    private String maskCardNumber(String cardNumber) {
        return "XXXX-XXXX-XXXX-" + cardNumber.substring(cardNumber.length() - 4);
    }

    private String maskCVV() {
        return "***";
    }

    private Card getCardAndVerifyOwnership(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));

        if (!card.getAccount().getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Card does not belong to user");
        }

        return card;
    }

    @Override
    public BankResponse createCard(CreateCardRequest cardRequest, Long userId) {

        User user = getUserById(userId);

        Account account = findAccountById(cardRequest.getAccountId());

        Card card = Card.builder()
                .account(account)
                .cardNumber(generateCardNumber())
                .cardType(cardRequest.getCardType().toString())
                .expiryDate(generateExpiryDate())
                .cvv(generateCVV())
                .status(CardStatus.ACTIVE.toString())
                .audit(Audit.builder()
                        .createdAt(new Date())
                        .createdBy(user)
                        .build())
                .build();

        Card savedCard = cardRepository.save(card);

        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("Card created successfully")
                .data(convertToDTOWithFullDetails(savedCard))
                .build();
    }

    @Override
    public PagedResponse<CardDTO> getCardByQuery(QueryCardDTO dto) {
        Page<Card> cards = cardRepository.findAllCards(dto.getSearch(),
                PageRequest.of(dto.getPage(), dto.getTake()));

        Page<CardDTO> cardDTOS = cards.map(this::convertToDTO);

        return new PagedResponse<>(cardDTOS);
    }

    @Override
    public List<CardDTO> getCardsByUserId(Long userId) {
        List<Card> cards = cardRepository.findByUserId(userId);

        return cards.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CardDTO getCardById(Long cardId, Long userId) {
        Card card = getCardAndVerifyOwnership(cardId, userId);

        return convertToDTO(card);
    }

    @Override
    @Transactional
    public BankResponse blockCard(Long cardId, Long userId) {
        Card card = getCardAndVerifyOwnership(cardId, userId);

        if (card.getStatus().equals(CardStatus.BLOCKED.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card is already blocked");
        }

        card.setStatus(CardStatus.BLOCKED.toString());
        cardRepository.save(card);

        return BankResponse.builder()
                .responseCode("202")
                .responseMessage("Card blocked successfully")
                .build();
    }

    @Override
    @Transactional
    public BankResponse activateCard(Long cardId, Long userId) {
        Card card = getCardAndVerifyOwnership(cardId, userId);

        if (card.getStatus().equals(CardStatus.ACTIVE.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card is already active");
        }

        card.setStatus(CardStatus.ACTIVE.toString());
        cardRepository.save(card);

        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("Card activated successfully")
                .build();
    }

    @Override
    public BankResponse deleteCard(Long cardId, Long userId) {
        Card card = getCardAndVerifyOwnership(cardId, userId);

        Audit audit = card.getAudit();
        audit.setDeletedAt(new Date());
        audit.setDeletedBy(userRepository.findById(userId).orElse(null));
        card.setAudit(audit);

        cardRepository.save(card);

        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("Card deleted successfully")
                .build();
    }

}
