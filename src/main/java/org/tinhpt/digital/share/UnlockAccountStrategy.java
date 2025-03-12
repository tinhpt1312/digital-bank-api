package org.tinhpt.digital.share;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.AccountRequest;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.repository.AccountRepository;
import org.tinhpt.digital.share.Strategy.RequestStrategy;
import org.tinhpt.digital.type.AccountStatus;
import org.tinhpt.digital.type.RequestType;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UnlockAccountStrategy implements RequestStrategy {

    private final AccountRepository accountRepository;


    @Override
    public void process(AccountRequest request, Long userId) {
        Long accountId = request.getAccount().getId();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(account.getUser().getId().longValue() != userId.longValue()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not permission in this account");
        }

        account.setStatus(AccountStatus.ACTIVE.toString());

        Audit audit = account.getAudit();
        audit.setUpdatedAt(new Date());

        accountRepository.save(account);
    }

    @Override
    public String getRequestType() {
        return RequestType.UNLOCK_ACCOUNT.toString();
    }
}
