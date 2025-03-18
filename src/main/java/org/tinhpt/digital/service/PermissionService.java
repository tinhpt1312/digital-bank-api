package org.tinhpt.digital.service;

import java.util.List;

import org.tinhpt.digital.dto.PermissionDto;
import org.tinhpt.digital.dto.request.CreatePermissionRequest;
import org.tinhpt.digital.dto.request.DeletePermissions;
import org.tinhpt.digital.dto.response.BankResponse;

public interface PermissionService {
    List<PermissionDto> getAllPermissions();

    BankResponse createPermission(CreatePermissionRequest createPermissionRequest, Long userId);

    BankResponse deletePermission(Long id, Long userId);

    BankResponse deleteMutilPermission(DeletePermissions deletePermissions, Long userId);

    BankResponse getPermissionsById(Long id);

}
