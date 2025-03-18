package org.tinhpt.digital.dto.request;

import org.tinhpt.digital.dto.PaginationOptionDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QueryCardDTO extends PaginationOptionDto {
    private String search;
}