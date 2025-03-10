package org.tinhpt.digital.type;


import com.fasterxml.jackson.annotation.JsonValue;


public enum AccountType {
    SAVINGS("SAVINGS"),
    CURRENT("CURRENT"),
    BUSINESS("BUSINESS");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}

