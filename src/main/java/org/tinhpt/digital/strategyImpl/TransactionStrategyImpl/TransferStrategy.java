package org.tinhpt.digital.strategyImpl.TransactionStrategyImpl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.request.TransactionRequest;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.Transaction;
import org.tinhpt.digital.strategy.TransactionStrategy;
import org.tinhpt.digital.type.TransactionType;


@Component
public class TransferStrategy implements TransactionStrategy {
    @Override
    public void processTransaction(TransactionRequest request, Account sourceAccount, Account destinationAccount, Transaction transaction) {
        if(destinationAccount == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination account is required for transfer");
        }

        if(sourceAccount.getBalance().compareTo(request.getAmount()) < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance for transfer");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TRANSFER;
    }
}
