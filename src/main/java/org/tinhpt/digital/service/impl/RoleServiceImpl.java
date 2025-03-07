package org.tinhpt.digital.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.RoleDTO;
import org.tinhpt.digital.dto.request.QueryRoleDto;
import org.tinhpt.digital.dto.request.RoleRequest;
import org.tinhpt.digital.dto.request.RolesDeleteDto;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.entity.Permission;
import org.tinhpt.digital.entity.Role;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.repository.PermissionRepository;
import org.tinhpt.digital.repository.RoleRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.RoleService;

import java.time.LocalDateTime;
import java.util.*;

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

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

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
    public BankResponse updateRole(Long id, RoleRequest request, Long userId) throws BadRequestException {
        Role role = roleRepository.findById(id).orElseThrow(() -> new BadRequestException("Role is not found"));

        role.getPermissions().clear();

        if(request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()){
            List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());
            role.setPermissions(new HashSet<>(permissions));
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        role.setName(request.getName());

        Audit audit = role.getAudit();
        audit.setUpdatedBy(user);
        audit.setUpdatedAt(new Date());

        roleRepository.save(role);

        return BankResponse.builder()
                .responseCode("201")
                .responseMessage("Update role is successfully")
                .build();
    }

    @Override
    public BankResponse deleteRole(Long id, Long userId) throws BadRequestException {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        role.getPermissions().clear();

        Audit audit = role.getAudit();
        audit.setDeletedBy(user);
        audit.setDeletedAt(new Date());

        roleRepository.save(role);

        return BankResponse.builder()
                .responseCode("201")
                .responseMessage("Delete role is successfully")
                .build();
    }

    @Override
    @Transactional
    public BankResponse deleteMultipleRoles(RolesDeleteDto dto, Long userId) throws BadRequestException {
        List<Long> ids = dto.getIds();
        if(ids == null || ids.isEmpty()){
            throw new BadRequestException("List of role ids is empty or null");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Role> roles = roleRepository.findAllById(ids);

        if(roles.size() != ids.size()){
            throw new BadRequestException("Some roles not found");
        }

        for(Role role: roles){
            role.getPermissions().clear();
            Audit audit = role.getAudit();
            audit.setDeletedBy(user);
            audit.setDeletedAt(new Date());
            roleRepository.save(role);
        }


        return BankResponse.builder()
                .responseCode("201")
                .responseMessage("Delete multiple role is successfully")
                .build();
    }
}