package org.tinhpt.digital.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinhpt.digital.annotation.CurrentUser;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.dto.PermissionDto;
import org.tinhpt.digital.dto.request.CreatePermissionRequest;
import org.tinhpt.digital.dto.request.DeletePermissions;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.service.PermissionService;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "Permission", description = "API for permission management")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping()
    @RequirePermission(subject = SubjectName.PERMISSION, action = PermissionsAction.READ)
    public List<PermissionDto> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    @GetMapping("/{id}")
    @RequirePermission(subject = SubjectName.PERMISSION, action = PermissionsAction.READ)
    public BankResponse getAllPermission(@PathVariable Long id) {
        return permissionService.getPermissionsById(id);
    }

    @PostMapping()
    @RequirePermission(subject = SubjectName.PERMISSION, action = PermissionsAction.CREATE)
    public BankResponse createPermission(@RequestBody() CreatePermissionRequest request,
            @CurrentUser() TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();

        return permissionService.createPermission(request, userId);
    }

    @DeleteMapping("/{id}")
    @RequirePermission(subject = SubjectName.PERMISSION, action = PermissionsAction.DELETE)
    public BankResponse deletePermission(
            @PathVariable Long id,
            @CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();
        return permissionService.deletePermission(id, userId);
    }

    @DeleteMapping("/delete-mutiple")
    @RequirePermission(subject = SubjectName.PERMISSION, action = PermissionsAction.DELETE)
    public BankResponse deleteMultiplePermissions(
            @RequestBody DeletePermissions deletePermissions,
            @CurrentUser TokenPayload tokenPayload) {
        Long userId = tokenPayload.getUserId();
        return permissionService.deleteMutilPermission(deletePermissions, userId);
    }
}