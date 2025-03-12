package org.tinhpt.digital.service;

import org.tinhpt.digital.dto.AccountDTO;
import org.tinhpt.digital.dto.request.CreateAccount;
import org.tinhpt.digital.dto.request.QueryAccountDTO;
import org.tinhpt.digital.dto.request.UpdateAccountDTO;
import org.tinhpt.digital.dto.request.UpdateBalanceAccountDTO;
import org.tinhpt.digital.dto.response.BankResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountDTO createAccount(CreateAccount createAccount, Long userId);

    List<AccountDTO> getAllAccounts();
    List<AccountDTO> getAllAccountsByUserId(Long userId);

    AccountDTO getAccountById(Long id);

    AccountDTO getAccountByQuery(QueryAccountDTO queryAccountDTO);

    AccountDTO updateAccount(Long id, UpdateAccountDTO updateAccountDTO, Long userId);

    AccountDTO updateBalance(Long id, UpdateBalanceAccountDTO updateBalanceAccountDTO, Long userId);

    BankResponse deleteAccount(Long id, Long userId);

    AccountDTO unlockAccount(Long id, Long userId);

    BankResponse submitBalanceUpdateRequest(UpdateBalanceAccountDTO updateBalanceAccountDTO, Long id, Long userId);

    BankResponse submitUnlockAccountRequest(Long id, Long userId);
}
