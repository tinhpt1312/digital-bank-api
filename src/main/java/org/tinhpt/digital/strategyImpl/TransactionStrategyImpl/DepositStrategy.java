package org.tinhpt.digital.strategyImpl.TransactionStrategyImpl;


import org.springframework.stereotype.Component;
import org.tinhpt.digital.dto.request.TransactionRequest;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.Transaction;
import org.tinhpt.digital.strategy.TransactionStrategy;
import org.tinhpt.digital.type.TransactionType;

@Component
public class DepositStrategy implements TransactionStrategy {

    @Override
    public void processTransaction(TransactionRequest request, Account sourceAccount, Account destinationAccount, Transaction transaction) {
            sourceAccount.setBalance(sourceAccount.getBalance().add(request.getAmount()));
    }


    @Override
    public TransactionType getTransactionType() {
        return TransactionType.DEPOSIT;
    }
}
