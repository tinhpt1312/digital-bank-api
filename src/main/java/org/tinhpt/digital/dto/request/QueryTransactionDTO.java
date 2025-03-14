package org.tinhpt.digital.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tinhpt.digital.dto.PaginationOptionDto;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QueryTransactionDTO extends PaginationOptionDto {
    private String search;
}
