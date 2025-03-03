package org.tinhpt.digital.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.tinhpt.digital.dto.request.RoleRequest;
import org.tinhpt.digital.entity.Role;
import org.tinhpt.digital.repository.RoleRepository;
import org.tinhpt.digital.service.RoleService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<String> createRole(RoleRequest request) {
        Optional<Role> role = roleRepository.findByName("USER");
        if (role.isPresent()) {
            return ResponseEntity.ok("Role is present");
        }

        Role newRole = Role.builder()
                .name(request.getName())
                .build();

        roleRepository.save(newRole);

        return ResponseEntity.ok("Create role is successfully");
    }
}
