package org.tinhpt.digital.dto;


import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationOptionDto {
    private int page;
    private int take;
}
