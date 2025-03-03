package org.tinhpt.digital.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinhpt.digital.dto.request.RoleRequest;
import org.tinhpt.digital.service.RoleService;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Api Role")
public class RoleController {

    private final RoleService roleService;

    @PostMapping()
    public ResponseEntity<String> createRole(@RequestBody RoleRequest request){
        return roleService.createRole(request);
    }

}
