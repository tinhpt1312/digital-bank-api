package org.tinhpt.digital.service;

import org.springframework.http.ResponseEntity;
import org.tinhpt.digital.dto.request.RoleRequest;

public interface RoleService {
    ResponseEntity<String> createRole(RoleRequest request);
}
