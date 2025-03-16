package org.tinhpt.digital.strategyImpl.RequestStrategyImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.request.TransactionRequest;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.AccountRequest;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.repository.AccountRepository;
import org.tinhpt.digital.strategy.RequestStrategy;
import org.tinhpt.digital.type.RequestType;
import org.tinhpt.digital.type.TransactionType;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UpdateBalanceStrategy implements RequestStrategy {

    private final AccountRepository accountRepository;

    @Override
    public void processRequest(AccountRequest request, Long userId) {

        Long accountId = request.getAccount().getId();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        BigDecimal amount = new BigDecimal(request.getDetails());
        BigDecimal newBalance = account.getBalance().add(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setBalance(newBalance);

        Audit audit = account.getAudit();
        audit.setUpdatedAt(new Date());

        accountRepository.save(account);
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.UPDATE_BALANCE;
    }

    @Override
    public TransactionRequest buildTransactionRequest(AccountRequest accountRequest, Long userId) throws Exception {
        BigDecimal amount = new BigDecimal(accountRequest.getDetails());

        return TransactionRequest.builder()
                .amount(amount)
                .transactionType(TransactionType.DEPOSIT)
                .currency(accountRequest.getAccount().getCurrency())
                .description("Deposit approved for account number: " + accountRequest.getAccount().getAccountNumber())
                .accountId(accountRequest.getAccount().getId())
                .destinationAccountId(null)
                .build();
    }

    @Override
    public boolean isTransactionRequired() {
        return true;
    }
}
