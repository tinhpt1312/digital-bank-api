package org.tinhpt.digital.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.Transaction;

public interface StatementService {
    Resource generateAccountStatementPdf(Account account, List<Transaction> transactions) throws IOException;

    void sendAccountStatementByEmail(Account account, List<Transaction> transactions, String email)
            throws IOException;
}
