package org.tinhpt.digital.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tinhpt.digital.annotation.CurrentUser;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.dto.CardDTO;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.request.CreateCardRequest;
import org.tinhpt.digital.dto.request.QueryCardDTO;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.service.CardService;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
@Tag(name = "Card", description = "Api card")
public class CardController {
    private final CardService cardService;

    @GetMapping()
    @RequirePermission(subject = SubjectName.CARD, action = PermissionsAction.READ)
    public ResponseEntity<PagedResponse<CardDTO>> getAllCards(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int take) {

        QueryCardDTO dto = new QueryCardDTO();
        dto.setSearch(search);
        dto.setPage(page - 1);
        dto.setTake(take);

        PagedResponse<CardDTO> response = cardService.getCardByQuery(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    @RequirePermission(subject = SubjectName.CARD, action = PermissionsAction.READ)
    public ResponseEntity<List<CardDTO>> getCardsByUserId(@CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();

        List<CardDTO> response = cardService.getCardsByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @RequirePermission(subject = SubjectName.CARD, action = PermissionsAction.READ)
    public ResponseEntity<CardDTO> getCardById(@PathVariable Long id, @CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();

        CardDTO response = cardService.getCardById(id, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    @RequirePermission(subject = SubjectName.CARD, action = PermissionsAction.CREATE)
    public BankResponse createCard(@CurrentUser TokenPayload tokenPayload,
            @RequestBody() CreateCardRequest cardRequest) {
        Long userId = tokenPayload.getUserId();

        return cardService.createCard(cardRequest, userId);
    }

    @PatchMapping("/block/{id}")
    @RequirePermission(subject = SubjectName.CARD, action = PermissionsAction.UPDATE)
    public BankResponse blockCard(@PathVariable Long id, @CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();
        return cardService.blockCard(id, userId);
    }

    @PatchMapping("/active/{id}")
    @RequirePermission(subject = SubjectName.CARD, action = PermissionsAction.UPDATE)
    public BankResponse activeCard(@PathVariable Long id, @CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();
        return cardService.activateCard(id, userId);
    }

    @DeleteMapping("/{id}")
    @RequirePermission(subject = SubjectName.CARD, action = PermissionsAction.DELETE)
    public BankResponse deleteCard(@PathVariable Long id, @CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();

        return cardService.deleteCard(id, userId);
    }
}