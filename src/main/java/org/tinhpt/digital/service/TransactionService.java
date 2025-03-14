package org.tinhpt.digital.service;

import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.TransactionDTO;
import org.tinhpt.digital.dto.request.QueryTransactionDTO;
import org.tinhpt.digital.dto.request.TransactionRequest;

public interface TransactionService {
    void createTransaction(TransactionRequest transactionRequest, Long userId);
    PagedResponse<TransactionDTO> findAll(QueryTransactionDTO dto);
}
