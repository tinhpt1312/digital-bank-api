package org.tinhpt.digital.dto;

import lombok.Builder;
import lombok.Data;
import org.tinhpt.digital.type.RequestType;

@Data
@Builder
public class AccountRequestDTO {
    private Long id;
    private Long requestBy;
    private RequestType requestType;
    private Long accountId;
    private String details;
}
