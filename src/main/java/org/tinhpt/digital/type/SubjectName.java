package org.tinhpt.digital.type;

import lombok.Getter;

@Getter
public enum SubjectName {
    ALL("all"),
    CUSTOMER("customers"),
    USER("users"),
    ROLE("roles"),
    ACCOUNT("accounts"),
    TRANSACTION("transactions"),
    PERMISSION("permissions"),
    BENEFICIARY("beneficiaries"),
    REPORT("reports"),
    NOTIFICATION("notifications"),
    SETTINGS("settings"),
    KYC("kyc"),
    LOAN("loans"),
    CARD("cards");

    private final String subject;

    SubjectName(String subject) {
        this.subject = subject;
    }

    public boolean canAccess(SubjectName requiredSubject) {
        if (this == ALL)
            return true;
        return this == requiredSubject;
    }
}
