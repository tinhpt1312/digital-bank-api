package org.tinhpt.digital.service;

import org.tinhpt.digital.dto.AccountDTO;
import org.tinhpt.digital.dto.request.CreateAccount;
import org.tinhpt.digital.dto.request.UpdateAccountDTO;
import org.tinhpt.digital.dto.response.BankResponse;

import java.util.List;

public interface AccountService {

    AccountDTO createAccount(CreateAccount createAccount, Long userId);

    List<AccountDTO> getAllAccounts();
    List<AccountDTO> getAllAccountsByUserId(Long userId);

    AccountDTO getAccountById(Long id);

    AccountDTO getAccountByAccountNumber(String accountNumber);

    AccountDTO updateAccount(Long id, UpdateAccountDTO updateAccountDTO, Long userId);
}
