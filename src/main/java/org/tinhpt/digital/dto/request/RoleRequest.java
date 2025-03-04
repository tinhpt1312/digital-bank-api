package org.tinhpt.digital.dto.request;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    private String name;

    private Set<Long> permissionIds;


}
