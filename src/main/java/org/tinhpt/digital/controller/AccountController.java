package org.tinhpt.digital.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tinhpt.digital.annotation.CurrentUser;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.dto.AccountDTO;
import org.tinhpt.digital.dto.request.CreateAccount;
import org.tinhpt.digital.dto.request.QueryAccountDTO;
import org.tinhpt.digital.dto.request.UpdateAccountDTO;
import org.tinhpt.digital.dto.request.UpdateBalanceAccountDTO;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.service.AccountService;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Api account")
public class AccountController {

    private final AccountService accountService;


    @GetMapping()
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.READ)
    public List<AccountDTO> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @GetMapping("/user")
    public List<AccountDTO> getAccountByUserId(@CurrentUser TokenPayload tokenPayload){
        Long userId = tokenPayload.getUserId();

        return accountService.getAllAccountsByUserId(userId);
    }

    @GetMapping("/search")
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.READ)
    public AccountDTO getAccountByQuery(@RequestParam() QueryAccountDTO queryAccountDTO){
        return accountService.getAccountByQuery(queryAccountDTO);
    }

    @GetMapping("/{id}")
    public AccountDTO getAccountById(@PathVariable Long id){
        return accountService.getAccountById(id);
    }


    @PostMapping()
    public AccountDTO createAccount(@CurrentUser TokenPayload tokenPayload, @RequestBody() CreateAccount createAccount){
        Long userId = tokenPayload.getUserId();

        return accountService.createAccount(createAccount, userId);
    }

    @PatchMapping("/{id}")
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.UPDATE)
    public AccountDTO updateAccount(@PathVariable() Long id, @CurrentUser TokenPayload tokenPayload,@RequestBody() UpdateAccountDTO updateAccountDTO){
        Long userId = tokenPayload.getUserId();

        return accountService.updateAccount(id, updateAccountDTO, userId);
    }

    @PatchMapping("/request-balance/user/{id}")
    public BankResponse submitBalanceUpdateRequest(@PathVariable() Long id, @CurrentUser TokenPayload tokenPayload
            ,@RequestBody() UpdateBalanceAccountDTO updateBalanceAccountDTO){
        Long userId = tokenPayload.getUserId();

        return accountService.submitBalanceUpdateRequest(updateBalanceAccountDTO, id, userId);
    }

    @PatchMapping("/request-unlock/user/{id}")
    public BankResponse unlockAccountRequest(@PathVariable() Long id, @CurrentUser TokenPayload tokenPayload){
        Long userId = tokenPayload.getUserId();

        return accountService.submitUnlockAccountRequest(id, userId);
    }

    @PatchMapping("/unlock/{id}")
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.UPDATE)
    public AccountDTO unlockAccount(@PathVariable() Long id, @CurrentUser TokenPayload tokenPayload){
        Long userId = tokenPayload.getUserId();

        return accountService.unlockAccount(id, userId);
    }

    @PatchMapping("/balance/{id}")
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.UPDATE)
    public AccountDTO updateBalance(@PathVariable() Long id, @RequestBody() UpdateBalanceAccountDTO updateBalanceAccountDTO, @CurrentUser TokenPayload tokenPayload){
        Long userId = tokenPayload.getUserId();

        return accountService.updateBalance(id, updateBalanceAccountDTO, userId);
    }

    @DeleteMapping("/{id}")
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.DELETE)
    public BankResponse deleteAccount(@PathVariable() Long id,@CurrentUser TokenPayload tokenPayload){
        Long userId = tokenPayload.getUserId();

        return accountService.deleteAccount(id, userId);
    }
}
