package org.tinhpt.digital.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tinhpt.digital.annotation.CurrentUser;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.dto.AccountDTO;
import org.tinhpt.digital.dto.request.CreateAccount;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.service.AccountService;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.AccountType;
import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Api account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping()
    public AccountDTO createAccount(@CurrentUser TokenPayload tokenPayload, @RequestBody() CreateAccount createAccount){
        Long userId = tokenPayload.getUserId();

        return accountService.createAccount(createAccount, userId);
    }

    @GetMapping()
    @RequirePermission(subject = SubjectName.ACCOUNT, action = PermissionsAction.READ)
    public List<AccountDTO> getAllAccounts(){
        return accountService.getAllAccounts();
    }
}
