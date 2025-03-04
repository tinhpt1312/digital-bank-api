package org.tinhpt.digital.service;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.RoleDTO;
import org.tinhpt.digital.dto.request.QueryRoleDto;
import org.tinhpt.digital.dto.request.RoleRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.entity.Role;

public interface RoleService {

    PagedResponse<RoleDTO> findAll(QueryRoleDto dto);

    BankResponse createRole(RoleRequest request, Long userId);

    BankResponse updateRole(Long id, RoleRequest request, Long userId);
}
