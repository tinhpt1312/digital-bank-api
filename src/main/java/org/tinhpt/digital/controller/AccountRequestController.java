package org.tinhpt.digital.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinhpt.digital.annotation.CurrentUser;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.dto.AccountRequestDTO;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.request.QueryAccountRequestDTO;
import org.tinhpt.digital.dto.request.UpdateBalanceAccountDTO;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.service.AccountRequestService;
import org.tinhpt.digital.service.AccountService;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

@RestController
@RequestMapping("/api/request-account")
@RequiredArgsConstructor
@Tag(name = "Account Request", description = "Api request account")
public class AccountRequestController {

    private final AccountRequestService accountRequestService;
    private final AccountService accountService;

    @GetMapping()
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.UPDATE)
    public ResponseEntity<PagedResponse<AccountRequestDTO>> getAllRequest(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int take
    ){
        QueryAccountRequestDTO dto = new QueryAccountRequestDTO();
        dto.setPage(page - 1);
        dto.setTake(take);

        PagedResponse<AccountRequestDTO> accountRequests = accountRequestService.getAllRequest(dto);

        return ResponseEntity.ok(accountRequests);
    }



    @PostMapping("/balance/{id}")
    public BankResponse submitBalanceUpdateRequest(@PathVariable() Long id, @CurrentUser TokenPayload tokenPayload
            ,@RequestBody() UpdateBalanceAccountDTO updateBalanceAccountDTO){
        Long userId = tokenPayload.getUserId();

        return accountService.submitBalanceUpdateRequest(updateBalanceAccountDTO, id, userId);
    }

    @PostMapping("/unlock/{id}")
    public BankResponse unlockAccountRequest(@PathVariable() Long id, @CurrentUser TokenPayload tokenPayload){
        Long userId = tokenPayload.getUserId();

        return accountService.submitUnlockAccountRequest(id, userId);
    }


    @PatchMapping("/approve/{id}")
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.UPDATE)
    public BankResponse approveRequest(@CurrentUser TokenPayload tokenPayload, @PathVariable("id") Long requestId) throws Exception {
        Long userId = tokenPayload.getUserId();

        return accountRequestService.approveRequest(requestId, userId);
    }
}
