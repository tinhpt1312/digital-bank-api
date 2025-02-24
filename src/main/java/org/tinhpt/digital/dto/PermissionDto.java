package org.tinhpt.digital.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
    private String name;
    private String action;
}
