package org.tinhpt.digital.service;

import org.apache.coyote.BadRequestException;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.RoleDTO;
import org.tinhpt.digital.dto.request.QueryRoleDto;
import org.tinhpt.digital.dto.request.RoleRequest;
import org.tinhpt.digital.dto.request.RolesDeleteDto;
import org.tinhpt.digital.dto.response.BankResponse;

public interface RoleService {

    PagedResponse<RoleDTO> findAll(QueryRoleDto dto);

    BankResponse createRole(RoleRequest request, Long userId);

    BankResponse updateRole(Long id, RoleRequest request, Long userId) throws BadRequestException;

    BankResponse deleteRole(Long id, Long userId) throws BadRequestException;

    BankResponse deleteMultipleRoles(RolesDeleteDto dto, Long userId) throws BadRequestException;

    BankResponse getRoleById(Long id);
}
