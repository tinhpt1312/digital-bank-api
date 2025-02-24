package org.tinhpt.digital.dto;

import lombok.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
    private String name;
    private String action;
}
