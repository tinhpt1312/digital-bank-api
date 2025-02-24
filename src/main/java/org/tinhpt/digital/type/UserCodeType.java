package org.tinhpt.digital.type;

import lombok.Getter;

@Getter
public enum UserCodeType {
    REGISTER(5),
    RESET_PASSWORD(3);

    private final int expirationMinutes;

    UserCodeType(int expirationMinutes) {
        this.expirationMinutes = expirationMinutes;
    }

}
