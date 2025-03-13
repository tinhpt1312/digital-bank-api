package org.tinhpt.digital.strategyImpl.TransactionStrategyImpl;


import org.springframework.stereotype.Component;
import org.tinhpt.digital.dto.request.TransactionRequest;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.Transaction;
import org.tinhpt.digital.strategy.TransactionStrategy;
import org.tinhpt.digital.type.TransactionType;

@Component
public class PaymentStrategy implements TransactionStrategy {
    @Override
    public void processTransaction(TransactionRequest request, Account sourceAccount, Account destinationAccount, Transaction transaction) {
        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance for payment");
        }
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
    }


    @Override
    public TransactionType getTransactionType() {
        return TransactionType.PAYMENT;
    }
}
