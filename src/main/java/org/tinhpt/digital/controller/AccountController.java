package org.tinhpt.digital.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.tinhpt.digital.annotation.CurrentUser;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.dto.AccountDTO;
import org.tinhpt.digital.dto.request.CreateAccount;
import org.tinhpt.digital.dto.request.QueryAccountDTO;
import org.tinhpt.digital.dto.request.TransferBankDTO;
import org.tinhpt.digital.dto.request.UpdateAccountDTO;
import org.tinhpt.digital.dto.request.UpdateBalanceAccountDTO;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.service.AccountService;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Api account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping()
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.READ)
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/user")
    public List<AccountDTO> getAccountByUserId(@CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();

        return accountService.getAllAccountsByUserId(userId);
    }

    @GetMapping("/search")
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.READ)
    public AccountDTO getAccountByQuery(@RequestParam() QueryAccountDTO queryAccountDTO) {
        return accountService.getAccountByQuery(queryAccountDTO);
    }

    @GetMapping("/{id}")
    public AccountDTO getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping()
    public AccountDTO createAccount(@CurrentUser TokenPayload tokenPayload,
            @RequestBody() CreateAccount createAccount) {
        Long userId = tokenPayload.getUserId();

        return accountService.createAccount(createAccount, userId);
    }

    @PostMapping("/statement/{id}")
    public BankResponse sendAccountStatementByEmail(
            @PathVariable("id") Long accountId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @CurrentUser TokenPayload tokenPayload) throws IOException {

        Long userId = tokenPayload.getUserId();

        return accountService.sendAccountStatementByEmail(accountId, fromDate, toDate, userId);
    }

    @PatchMapping("/{id}")
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.UPDATE)
    public AccountDTO updateAccount(@PathVariable() Long id, @CurrentUser TokenPayload tokenPayload,
            @RequestBody() UpdateAccountDTO updateAccountDTO) {
        Long userId = tokenPayload.getUserId();

        return accountService.updateAccount(id, updateAccountDTO, userId);
    }

    @PatchMapping("/unlock/{id}")
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.UPDATE)
    public AccountDTO unlockAccount(@PathVariable() Long id, @CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();

        return accountService.unlockAccount(id, userId);
    }

    @PatchMapping("/withdrawal/{id}")
    public AccountDTO withDrawlBalance(@PathVariable() Long id,
            @RequestBody() UpdateBalanceAccountDTO updateBalanceAccountDTO, @CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();

        return accountService.withDrawlBalance(updateBalanceAccountDTO, id, userId);
    }

    @PatchMapping("/transfer")
    public BankResponse transferBalance(@RequestBody() TransferBankDTO transferBankDTO,
            @CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();

        return accountService.transferBank(transferBankDTO, userId);
    }

    @DeleteMapping("/{id}")
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.DELETE)
    public BankResponse deleteAccount(@PathVariable() Long id, @CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();

        return accountService.deleteAccount(id, userId);
    }
}
