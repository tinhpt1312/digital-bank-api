package org.tinhpt.digital.utils;

public enum SystemPermission {
    // Quyền quản lý người dùng
    USER_CREATE,
    USER_READ,
    USER_UPDATE,
    USER_DELETE,
    
    // Quyền quản lý tài khoản
    ACCOUNT_CREATE,
    ACCOUNT_READ,
    ACCOUNT_UPDATE,
    ACCOUNT_DELETE,
    
    // Quyền quản lý giao dịch
    TRANSACTION_CREATE,
    TRANSACTION_READ,
    TRANSACTION_UPDATE,
    TRANSACTION_DELETE,
    
    // Quyền quản lý khoản vay
    LOAN_CREATE,
    LOAN_READ,
    LOAN_UPDATE,
    LOAN_DELETE
}