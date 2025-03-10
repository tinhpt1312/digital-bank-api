package org.tinhpt.digital.service.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.AccountDTO;
import org.tinhpt.digital.dto.request.CreateAccount;
import org.tinhpt.digital.dto.request.UpdateAccountDTO;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.repository.AccountRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.AccountService;
import org.tinhpt.digital.type.AccountStatus;
import org.tinhpt.digital.type.AccountType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    private String generationAccountNumber(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < 10; i++){
            stringBuilder.append(random.nextInt(10));
        }

        return stringBuilder.toString();
    }

    private AccountDTO convertToDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(AccountType.valueOf(String.valueOf(account.getAccountType())))
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(AccountStatus.valueOf(String.valueOf(account.getStatus())))
                .userId(account.getUser().getId())
                .build();
    }

    @Override
    public AccountDTO createAccount(CreateAccount createAccount, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Account account = Account.builder()
                .accountType(String.valueOf(createAccount.getAccountType()))
                .currency(createAccount.getCurrency())
                .user(user)
                .accountNumber(generationAccountNumber())
                .status(AccountStatus.INACTIVE.toString())
                .balance(BigDecimal.ZERO)
                .audit(Audit.builder()
                        .createdBy(user)
                        .createdAt(new Date())
                        .build())
                .build();

        Account savedAccount = accountRepository.save(account);

        return convertToDTO(savedAccount);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public List<AccountDTO> getAllAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountById(Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with id: " + id));
        return convertToDTO(account);
    }

    @Override
    public AccountDTO getAccountByAccountNumber(String accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not is present with number: " + accountNumber));

        return convertToDTO(account);
    }

    @Transactional
    @Override
    public AccountDTO updateAccount(Long id, UpdateAccountDTO updateAccountDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with id: " + id));

        if (updateAccountDTO.getStatus() != null) {
            account.setStatus(updateAccountDTO.getStatus());
        }

        Audit audit = account.getAudit();
        audit.setUpdatedAt(new Date());
        audit.setUpdatedBy(user);

        Account updatedAccount = accountRepository.save(account);
        return convertToDTO(updatedAccount);
    }
}
