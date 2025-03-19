package org.tinhpt.digital.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.AccountDTO;
import org.tinhpt.digital.dto.request.CreateAccount;
import org.tinhpt.digital.dto.request.QueryAccountDTO;
import org.tinhpt.digital.dto.request.TransactionRequest;
import org.tinhpt.digital.dto.request.TransferBankDTO;
import org.tinhpt.digital.dto.request.UpdateAccountDTO;
import org.tinhpt.digital.dto.request.UpdateBalanceAccountDTO;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.AccountRequest;
import org.tinhpt.digital.entity.Transaction;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.repository.AccountRepository;
import org.tinhpt.digital.repository.AccountRequestRepository;
import org.tinhpt.digital.repository.TransactionRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.AccountService;
import org.tinhpt.digital.service.StatementService;
import org.tinhpt.digital.service.TransactionService;
import org.tinhpt.digital.type.AccountStatus;
import org.tinhpt.digital.type.AccountType;
import org.tinhpt.digital.type.RequestStatus;
import org.tinhpt.digital.type.RequestType;
import org.tinhpt.digital.type.TransactionType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

        private final AccountRepository accountRepository;
        private final UserRepository userRepository;
        private final AccountRequestRepository accountRequestRepository;
        private final TransactionService transactionService;
        private final StatementService statementService;
        private final TransactionRepository transactionRepository;

        private String generationAccountNumber() {
                Random random = new Random();
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < 10; i++) {
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

        private Account findAccountById(Long id) {
                return accountRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Account not found with id: " + id));
        }

        private User findUserById(Long id) {
                return userRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        }

        @Override
        public AccountDTO createAccount(CreateAccount createAccount, Long userId) {
                User user = findUserById(userId);

                Account account = Account.builder()
                                .accountType(String.valueOf(createAccount.getAccountType()))
                                .currency(createAccount.getCurrency())
                                .user(user)
                                .accountNumber(generationAccountNumber())
                                .status(AccountStatus.ACTIVE.toString())
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
        public AccountDTO getAccountById(Long id) {
                Account account = findAccountById(id);
                return convertToDTO(account);
        }

        @Override
        public AccountDTO getAccountByQuery(QueryAccountDTO queryAccountDTO) {
                return accountRepository.findById(queryAccountDTO.getId())
                                .or(() -> accountRepository.findByAccountNumber(queryAccountDTO.getAccountNumber()))
                                .map(this::convertToDTO)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Account not found"));
        }

        @Transactional
        @Override
        public AccountDTO updateAccount(Long id, UpdateAccountDTO updateAccountDTO, Long userId) {
                User user = findUserById(userId);

                Account account = findAccountById(id);

                if (updateAccountDTO.getStatus() != null) {
                        account.setStatus(updateAccountDTO.getStatus());
                }

                Audit audit = account.getAudit();
                audit.setUpdatedAt(new Date());
                audit.setUpdatedBy(user);

                Account updatedAccount = accountRepository.save(account);
                return convertToDTO(updatedAccount);
        }

        @Transactional
        @Override
        public AccountDTO withDrawlBalance(UpdateBalanceAccountDTO updateBalanceAccountDTO, Long id, Long userId) {
                User user = findUserById(userId);

                Account account = findAccountById(id);

                if (account.getStatus().equals(AccountStatus.INACTIVE.toString()) ||
                                account.getStatus().equals(AccountStatus.FROZEN.toString())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is locked");
                }

                if (account.getBalance().compareTo(updateBalanceAccountDTO.getAmount()) < 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Insufficient balance for withdrawal");
                }

                TransactionRequest transactionRequest = TransactionRequest.builder()
                                .amount(updateBalanceAccountDTO.getAmount())
                                .transactionType(TransactionType.WITHDRAWAL)
                                .currency(account.getCurrency())
                                .description("Deposit for account number: " + account.getAccountNumber())
                                .accountId(account.getId())
                                .destinationAccountId(null)
                                .build();

                Audit audit = account.getAudit();
                audit.setUpdatedAt(new Date());
                audit.setUpdatedBy(user);

                Account updateAccount = accountRepository.save(account);
                transactionService.createTransaction(transactionRequest, userId);

                return convertToDTO(updateAccount);
        }

        @Transactional
        @Override
        public BankResponse deleteAccount(Long id, Long userId) {
                User user = findUserById(userId);

                Account account = findAccountById(id);

                Audit audit = account.getAudit();
                audit.setDeletedAt(new Date());
                audit.setDeletedBy(user);

                accountRepository.save(account);

                return BankResponse.builder()
                                .responseCode("202")
                                .responseMessage("Delete account is successfully")
                                .build();
        }

        @Transactional
        @Override
        public AccountDTO unlockAccount(Long id, Long userId) {
                User user = findUserById(userId);

                Account account = findAccountById(id);

                if (account.getStatus().equals(AccountStatus.ACTIVE.toString())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is already active");
                }

                account.setStatus(AccountStatus.ACTIVE.toString());

                Audit audit = account.getAudit();
                audit.setUpdatedAt(new Date());
                audit.setUpdatedBy(user);

                Account updatedAccount = accountRepository.save(account);
                return convertToDTO(updatedAccount);
        }

        @Override
        public BankResponse submitBalanceUpdateRequest(UpdateBalanceAccountDTO updateBalanceAccountDTO, Long id,
                        Long userId) {

                Account account = findAccountById(id);

                User user = findUserById(userId);

                if (!account.getUser().getId().equals(userId)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "User does not have permission for this account");
                }

                AccountRequest accountRequest = AccountRequest.builder()
                                .account(account)
                                .requestedBy(user)
                                .requestType(RequestType.UPDATE_BALANCE.toString())
                                .details(updateBalanceAccountDTO.getAmount().toString())
                                .status(RequestStatus.PENDING)
                                .createdAt(new Date())
                                .build();

                accountRequestRepository.save(accountRequest);

                return BankResponse.builder()
                                .responseCode("202")
                                .responseMessage("Send request to Admin is successfully! Wait for Admin approved")
                                .build();
        }

        @Override
        public BankResponse submitUnlockAccountRequest(Long id, Long userId) {
                Account account = findAccountById(id);

                User user = findUserById(userId);

                if (!account.getUser().getId().equals(userId)) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "User does not have permission for this account");
                }

                AccountRequest accountRequest = AccountRequest.builder()
                                .account(account)
                                .requestedBy(user)
                                .requestType(RequestType.UNLOCK_ACCOUNT.toString())
                                .details("Unlock account for this user")
                                .status(RequestStatus.PENDING)
                                .createdAt(new Date())
                                .build();

                accountRequestRepository.save(accountRequest);

                return BankResponse.builder()
                                .responseCode("202")
                                .responseMessage("Send request to Admin is successfully! Wait for Admin approved")
                                .build();
        }

        @Transactional
        @Override
        public BankResponse transferBank(TransferBankDTO transferBankDTO, Long userId) {

                Account sourceAccount = findAccountById(transferBankDTO.getSourceACcount());
                Account targetAccount = findAccountById(transferBankDTO.getTargetAccount());

                if (!sourceAccount.getUser().getId().equals(userId) || sourceAccount.getId() == targetAccount.getId()) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "User does not have permission for this account");
                }

                if (!sourceAccount.getCurrency().equals(targetAccount.getCurrency())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Source account and target account must have the same currency");
                }

                if (transferBankDTO.getAmount().compareTo(sourceAccount.getBalance()) > 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                        "Amount must be less than or equal to balance");
                }

                TransactionRequest transactionRequest = TransactionRequest.builder()
                                .currency(sourceAccount.getCurrency())
                                .description("Transfer money from " + sourceAccount.getAccountNumber() + " to " + " "
                                                + targetAccount.getAccountNumber() + " with amount "
                                                + transferBankDTO.getAmount()
                                                + " VND")
                                .transactionType(TransactionType.TRANSFER)
                                .accountId(sourceAccount.getId())
                                .destinationAccountId(targetAccount.getId())
                                .amount(transferBankDTO.getAmount())
                                .build();

                transactionService.createTransaction(transactionRequest, userId);

                return BankResponse.builder()
                                .responseCode("202")
                                .responseMessage("Transfer money successfully!")
                                .build();
        }

        @Override
        public BankResponse sendAccountStatementByEmail(Long accountId, Date fromDate, Date toDate,
                        Long userId) throws IOException {
                Account account = findAccountById(accountId);

                if (account.getUser().getId() != userId) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                                        "User does not have permission for this account");
                }

                List<Transaction> transactions;
                if (fromDate != null && toDate != null) {
                        transactions = transactionRepository.findTransactionsByAccountAndDateRange(account.getId(),
                                        fromDate,
                                        toDate);
                } else {
                        transactions = account.getTransactions();
                }

                User user = account.getUser();
                String email = user.getEmail();

                statementService.sendAccountStatementByEmail(account, transactions, email);

                return BankResponse.builder()
                                .responseCode("202")
                                .responseMessage("Export statement is successfullt! pls check your email")
                                .build();
        }

}
