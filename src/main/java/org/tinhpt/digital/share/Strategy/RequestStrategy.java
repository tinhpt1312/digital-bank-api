package org.tinhpt.digital.share.Strategy;

import org.tinhpt.digital.entity.AccountRequest;

public interface RequestStrategy {
    void process(AccountRequest request, Long userId);
    String getRequestType();
}
