package org.tinhpt.digital.share;

import org.tinhpt.digital.dto.PermissionDto;

import java.util.Set;

public interface ITokenPayload {
    Long getUserId();

    String getUserName();

    Set<PermissionDto> getPermissions();
}
