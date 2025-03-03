package org.tinhpt.digital.share;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinhpt.digital.dto.PermissionDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenPayload implements ITokenPayload{
    private Long userId;
    private String username;
    private Set<PermissionDto> permissions;

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public Set<PermissionDto> getPermissions() {
        return permissions;
    }
}
