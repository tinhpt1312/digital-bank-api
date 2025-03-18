package org.tinhpt.digital.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {
    private String responseCode;

    private String responseMessage;

    private Object data;

    public static class BankResponseBuilder {
        public BankResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }
    }
}
