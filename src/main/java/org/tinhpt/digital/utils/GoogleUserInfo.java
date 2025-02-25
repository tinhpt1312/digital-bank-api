package org.tinhpt.digital.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserInfo {
    private String email;
    private String name;
    private String pictureUrl;
}
