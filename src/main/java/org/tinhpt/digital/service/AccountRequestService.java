package org.tinhpt.digital.service;

import org.tinhpt.digital.dto.AccountRequestDTO;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.request.QueryAccountRequestDTO;
import org.tinhpt.digital.dto.response.BankResponse;


public interface AccountRequestService {
    BankResponse approveRequest(Long requestId, Long adminId) throws Exception;

    BankResponse rejectRequest(Long requestId, Long adminId) throws Exception;
    PagedResponse<AccountRequestDTO> getAllRequest(QueryAccountRequestDTO dto);
}
