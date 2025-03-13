package org.tinhpt.digital.service;

import org.tinhpt.digital.dto.TransactionDTO;
import org.tinhpt.digital.dto.request.TransactionRequest;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionRequest transactionRequest, Long userId);
}
