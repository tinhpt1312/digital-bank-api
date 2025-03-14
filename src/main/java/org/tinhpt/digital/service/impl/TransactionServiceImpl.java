package org.tinhpt.digital.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.TransactionDTO;
import org.tinhpt.digital.dto.request.QueryTransactionDTO;
import org.tinhpt.digital.dto.request.TransactionRequest;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.Transaction;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.factory.TransactionStrategyFactory;
import org.tinhpt.digital.repository.AccountRepository;
import org.tinhpt.digital.repository.TransactionRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.TransactionService;
import org.tinhpt.digital.strategy.TransactionStrategy;
import org.tinhpt.digital.type.TransactionStatus;
import org.tinhpt.digital.type.TransactionType;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionStrategyFactory strategyFactory;

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private TransactionDTO convertToDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .description(transaction.getDescription())
                .status(TransactionStatus.valueOf(transaction.getStatus()))
                .transactionType(TransactionType.valueOf(transaction.getTransactionType()))
                .accountId(transaction.getAccount().getId())
                .destinationAccountId(transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getId() : null)
                .build();
    }

    @Override
    @Transactional
    public void createTransaction(TransactionRequest transactionRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Account sourceAccount = accountRepository.findById(transactionRequest.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with id: " + transactionRequest.getAccountId()));

        Account destinationAccount = null;
        if (transactionRequest.getTransactionType() == TransactionType.TRANSFER) {
            if (transactionRequest.getDestinationAccountId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Destination account is required for TRANSFER");
            }
            destinationAccount = accountRepository.findById(transactionRequest.getDestinationAccountId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination account not found"));
        }

        Transaction transaction = Transaction.builder()
                .amount(transactionRequest.getAmount())
                .currency(transactionRequest.getCurrency())
                .description(transactionRequest.getDescription())
                .transactionType(transactionRequest.getTransactionType().toString())
                .account(sourceAccount)
                .destinationAccount(destinationAccount)
                .audit(Audit.builder()
                        .createdAt(new Date())
                        .createdBy(user)
                        .build())
                .build();

        try {
            TransactionStrategy strategy = strategyFactory.getTransaction(transactionRequest.getTransactionType());
            transaction.setStatus(TransactionStatus.COMPLETED.toString());
            strategy.processTransaction(transactionRequest, sourceAccount, destinationAccount, transaction);

            accountRepository.save(sourceAccount);

            if (destinationAccount != null) {
                accountRepository.save(destinationAccount);
            }

            Transaction saveTransaction = transactionRepository.save(transaction);

            convertToDTO(saveTransaction);
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED.toString());
            transactionRepository.save(transaction);
            throw new RuntimeException("Transaction failed: " + e.getMessage());
        }
    }

    @Override
    public PagedResponse<TransactionDTO> findAll(QueryTransactionDTO dto) {
        Page<Transaction> transactions = transactionRepository.findAllTransactions(dto.getSearch(),
                PageRequest.of(dto.getPage(), dto.getTake()));

        Page<TransactionDTO> transactionDTOPage = transactions.map(this::convertToDTO);

        return new PagedResponse<>(transactionDTOPage);
    }
}
