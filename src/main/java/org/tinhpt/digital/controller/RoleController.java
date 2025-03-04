package org.tinhpt.digital.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinhpt.digital.annotation.CurrentUser;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.RoleDTO;
import org.tinhpt.digital.dto.request.QueryRoleDto;
import org.tinhpt.digital.dto.request.RoleRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.service.RoleService;
import org.tinhpt.digital.share.TokenPayload;
import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Api Role")
public class RoleController {

    private final RoleService roleService;

    @PostMapping()
    @RequirePermission(subject = SubjectName.ROLE, action = PermissionsAction.CREATE)
    public BankResponse createRole(@RequestBody RoleRequest request, @CurrentUser TokenPayload tokenPayload){
        Long userId = tokenPayload.getUserId();
        return roleService.createRole(request, userId);
    }

    @GetMapping()
    @RequirePermission(subject = SubjectName.ROLE, action = PermissionsAction.READ)
    public ResponseEntity<PagedResponse<RoleDTO>> getAllRoles(@RequestParam(required = false) String search,
                                     @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int take){
        QueryRoleDto dto = new QueryRoleDto();
        dto.setSearch(search);
        dto.setPage(page - 1);
        dto.setTake(take);

        PagedResponse<RoleDTO> roles = roleService.findAll(dto);
        return ResponseEntity.ok(roles);
    }

}
