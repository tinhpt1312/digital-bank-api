package org.tinhpt.digital.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tinhpt.digital.annotation.CurrentUser;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.TransactionDTO;
import org.tinhpt.digital.dto.request.QueryTransactionDTO;
import org.tinhpt.digital.service.TransactionService;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Api transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping()
    @RequirePermission(subject = SubjectName.TRANSACTION, action = PermissionsAction.READ)
    public ResponseEntity<PagedResponse<TransactionDTO>> getAllTransaction(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int take) {
        QueryTransactionDTO dto = new QueryTransactionDTO();
        dto.setSearch(search);
        dto.setPage(page - 1);
        dto.setTake(take);

        PagedResponse<TransactionDTO> transactions = transactionService.findAll(dto);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/me")
    public List<TransactionDTO> getTransactionByUserId(@CurrentUser() TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();

        return transactionService.getTransactionById(userId);
    }

}
