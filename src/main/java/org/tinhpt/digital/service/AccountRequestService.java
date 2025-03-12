package org.tinhpt.digital.service;

import org.tinhpt.digital.dto.AccountRequestDTO;
import org.tinhpt.digital.dto.response.BankResponse;

import java.util.List;

public interface AccountRequestService {
    BankResponse approveRequest(Long requestId, Long adminId);
    List<AccountRequestDTO> getAllRequest();
}
