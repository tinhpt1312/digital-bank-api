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
import org.tinhpt.digital.type.AccountStatus;
import org.tinhpt.digital.type.RequestType;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UnlockAccountStrategy implements RequestStrategy {

    private final AccountRepository accountRepository;


    @Override
    public void processRequest(AccountRequest request, Long userId) {
        Long accountId = request.getAccount().getId();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        account.setStatus(AccountStatus.ACTIVE.toString());

        Audit audit = account.getAudit();
        audit.setUpdatedAt(new Date());

        accountRepository.save(account);
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.UNLOCK_ACCOUNT;
    }

    @Override
    public TransactionRequest buildTransactionRequest(AccountRequest accountRequest, Long userId) throws Exception {
        return null;
    }

    @Override
    public boolean isTransactionRequired() {
        return false;
    }
}
