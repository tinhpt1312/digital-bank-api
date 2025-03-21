package org.tinhpt.digital.service;

import org.tinhpt.digital.dto.AccountDTO;
import org.tinhpt.digital.dto.request.CreateAccount;
import org.tinhpt.digital.dto.request.QueryAccountDTO;
import org.tinhpt.digital.dto.request.TransferBankDTO;
import org.tinhpt.digital.dto.request.UpdateAccountDTO;
import org.tinhpt.digital.dto.request.UpdateBalanceAccountDTO;
import org.tinhpt.digital.dto.response.BankResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface AccountService {

    AccountDTO createAccount(CreateAccount createAccount, Long userId);

    List<AccountDTO> getAllAccounts();

    List<AccountDTO> getAllAccountsByUserId(Long userId);

    AccountDTO getAccountById(Long id);

    AccountDTO getAccountByQuery(QueryAccountDTO queryAccountDTO);

    AccountDTO updateAccount(Long id, UpdateAccountDTO updateAccountDTO, Long userId);

    AccountDTO withDrawlBalance(UpdateBalanceAccountDTO updateBalanceAccountDTO, Long id, Long userId);

    BankResponse deleteAccount(Long id, Long userId);

    AccountDTO unlockAccount(Long id, Long userId);

    BankResponse submitBalanceUpdateRequest(UpdateBalanceAccountDTO updateBalanceAccountDTO, Long id, Long userId);

    BankResponse submitUnlockAccountRequest(Long id, Long userId);

    BankResponse transferBank(TransferBankDTO transferBankDTO, Long userId);

    BankResponse sendAccountStatementByEmail(Long accountId, Date fromDate, Date toDate,
            Long userId) throws IOException;
}
