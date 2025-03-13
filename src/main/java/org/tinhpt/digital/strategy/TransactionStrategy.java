package org.tinhpt.digital.strategy;

import org.tinhpt.digital.dto.request.TransactionRequest;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.Transaction;
import org.tinhpt.digital.type.TransactionType;

public interface TransactionStrategy {
    void processTransaction(TransactionRequest request, Account sourceAccount, Account destinationAccount, Transaction transaction);

    TransactionType getTransactionType();

}
