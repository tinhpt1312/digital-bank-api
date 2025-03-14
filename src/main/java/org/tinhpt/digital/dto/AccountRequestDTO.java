package org.tinhpt.digital.dto;

import lombok.Builder;
import lombok.Data;
import org.tinhpt.digital.entity.AccountRequest;
import org.tinhpt.digital.entity.Role;
import org.tinhpt.digital.type.RequestStatus;
import org.tinhpt.digital.type.RequestType;

import java.util.stream.Collectors;

@Data
@Builder
public class AccountRequestDTO {
    private Long id;
    private Long requestBy;
    private RequestType requestType;
    private Long accountId;
    private String details;
    private RequestStatus requestStatus;
}
