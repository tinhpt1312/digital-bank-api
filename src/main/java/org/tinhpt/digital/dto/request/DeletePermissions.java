package org.tinhpt.digital.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class DeletePermissions {
    private List<Long> ids;
}
