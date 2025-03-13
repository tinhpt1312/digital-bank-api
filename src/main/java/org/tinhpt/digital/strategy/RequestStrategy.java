package org.tinhpt.digital.strategy;

import org.tinhpt.digital.dto.request.TransactionRequest;
import org.tinhpt.digital.entity.AccountRequest;
import org.tinhpt.digital.type.RequestType;

public interface RequestStrategy {
    void processRequest(AccountRequest request, Long userId);
    RequestType getRequestType();
    TransactionRequest buildTransactionRequest(AccountRequest accountRequest, Long userId) throws Exception;
    boolean isTransactionRequired();
}
