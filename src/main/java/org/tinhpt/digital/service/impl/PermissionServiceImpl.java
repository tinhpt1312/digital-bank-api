package org.tinhpt.digital.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.PermissionDto;
import org.tinhpt.digital.dto.request.CreatePermissionRequest;
import org.tinhpt.digital.dto.request.DeletePermissions;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.entity.Permission;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.repository.PermissionRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.PermissionService;
import org.tinhpt.digital.type.PermissionsAction;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

        private final PermissionRepository permissionRepository;
        private final UserRepository userRepository;

        private PermissionDto convertToDto(Permission permission) {
                return PermissionDto.builder()
                                .name(permission.getSubject().name())
                                .action(permission.getAction().name())
                                .build();
        }

        private User getUserById(Long userId) {
                return userRepository.findById(userId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        }

        private Permission getPermissionById(Long id) {
                return permissionRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Permission not found"));
        }

        @Override
        public List<PermissionDto> getAllPermissions() {
                List<Permission> permissions = permissionRepository.findAll();

                return permissions.stream()
                                .map(this::convertToDto)
                                .toList();
        }

        @Override
        @Transactional
        public BankResponse createPermission(CreatePermissionRequest createPermissionRequest, Long userId) {
                User user = getUserById(userId);

                List<Permission> permission = permissionRepository
                                .findBySubjectName(createPermissionRequest.getSubject());

                List<PermissionsAction> actionsToAdd = createPermissionRequest.getAction().stream()
                                .filter(action -> permission.stream()
                                                .noneMatch(p -> p.getAction() == action))
                                .toList();

                permission.clear();

                List<Permission> newPermissions = actionsToAdd.stream()
                                .map(action -> Permission.builder()
                                                .subject(createPermissionRequest.getSubject())
                                                .action(action)
                                                .isActive(true)
                                                .audit(Audit.builder()
                                                                .createdBy(user)
                                                                .createdAt(new Date())
                                                                .build())
                                                .build())
                                .toList();

                List<Permission> savedPermissions = permissionRepository.saveAll(newPermissions);

                List<PermissionDto> permissionDtos = savedPermissions.stream()
                                .map(this::convertToDto)
                                .toList();

                return BankResponse.builder()
                                .responseCode("202")
                                .responseMessage("Create permission successfully")
                                .data(permissionDtos)
                                .build();
        }

        @Override
        public BankResponse deletePermission(Long id, Long userId) {
                User user = getUserById(userId);
                Permission permission = getPermissionById(id);

                Audit audit = permission.getAudit();
                audit.setDeletedAt(new Date());
                audit.setDeletedBy(user);

                permissionRepository.save(permission);

                return BankResponse.builder()
                                .responseCode("200")
                                .responseMessage("Permission deleted successfully")
                                .build();
        }

        @Override
        public BankResponse getPermissionsById(Long id) {
                Permission permission = getPermissionById(id);

                PermissionDto permissionDto = convertToDto(permission);

                return BankResponse.builder()
                                .responseCode("202")
                                .responseMessage("Get permission successfully")
                                .data(permissionDto)
                                .build();
        }

        @Override
        public BankResponse deleteMutilPermission(DeletePermissions deletePermissions, Long userId) {
                User user = getUserById(userId);

                List<Permission> permissions = permissionRepository.findAllById(deletePermissions.getIds());

                if (permissions.isEmpty()) {
                        return BankResponse.builder()
                                        .responseCode("404")
                                        .responseMessage("No permissions found with the provided IDs")
                                        .build();
                }

                permissions.forEach(permission -> {
                        Audit audit = permission.getAudit();
                        audit.setDeletedAt(new Date());
                        audit.setDeletedBy(user);
                });

                permissionRepository.saveAll(permissions);

                return BankResponse.builder()
                                .responseCode("200")
                                .responseMessage("Permissions deleted successfully")
                                .build();
        }

}
