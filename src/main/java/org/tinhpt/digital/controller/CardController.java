package org.tinhpt.digital.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.dto.CardDTO;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.request.QueryCardDTO;
import org.tinhpt.digital.service.CardService;
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
}
