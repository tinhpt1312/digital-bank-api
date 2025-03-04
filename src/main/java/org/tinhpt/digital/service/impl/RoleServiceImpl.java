package org.tinhpt.digital.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.RoleDTO;
import org.tinhpt.digital.dto.request.QueryRoleDto;
import org.tinhpt.digital.dto.request.RoleRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.entity.Permission;
import org.tinhpt.digital.entity.Role;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.repository.PermissionRepository;
import org.tinhpt.digital.repository.RoleRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    @Override
    public PagedResponse<RoleDTO> findAll(QueryRoleDto dto) {
        Page<Role> roles = roleRepository.findAllRoles(dto.getSearch(),
                PageRequest.of(dto.getPage(), dto.getTake()));

        Page<RoleDTO> roleDTOPage = roles.map(RoleDTO::new);

        return new PagedResponse<>(roleDTOPage);
    }

    @Override
    public BankResponse createRole(RoleRequest request, Long userId) {
        Optional<Role> role = roleRepository.findByName(request.getName());
        if (role.isPresent()) {
            return BankResponse.builder()
                    .responseCode("")
                    .responseMessage("Role is present")
                    .build();
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role newRole = Role.builder()
                .name(request.getName())
                .audit(Audit.builder()
                        .createdBy(user)
                        .build())
                .build();

        Role savedRole = roleRepository.save(newRole);

        if(request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()){
            List<Permission> permissionsList = permissionRepository.findAllById(request.getPermissionIds());
            Set<Permission> permissionsSet = new HashSet<>(permissionsList);
            savedRole.setPermissions(permissionsSet);
            roleRepository.save(savedRole);
        }

        return BankResponse.builder()
                .responseCode("201")
                .responseMessage("Create role is successfully")
                .build();
    }

    @Override
    public BankResponse updateRole(Long id, RoleRequest request, Long userId) {
        return BankResponse.builder()
                .responseCode("201")
                .responseMessage("Update role is successfully")
                .build();
    }
}
