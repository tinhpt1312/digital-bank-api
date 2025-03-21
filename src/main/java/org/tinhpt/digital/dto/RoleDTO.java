package org.tinhpt.digital.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.tinhpt.digital.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RoleDTO {
    private Long id;
    private String name;
    private List<UserDTO> users;
    private List<PermissionDto> permissionDtos;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.users = role.getUsers().stream()
                .map(user -> new UserDTO(user.getId()))
                .collect(Collectors.toList());
        this.permissionDtos = role.getPermissions().stream()
                .map(permission -> new PermissionDto())
                .collect(Collectors.toList());
    }
}
