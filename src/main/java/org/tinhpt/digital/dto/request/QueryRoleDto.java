package org.tinhpt.digital.dto.request;

import lombok.*;
import org.tinhpt.digital.dto.PaginationOptionDto;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QueryRoleDto extends PaginationOptionDto {
    private String search;
}
