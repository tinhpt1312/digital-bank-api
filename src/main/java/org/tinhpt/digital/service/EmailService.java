package org.tinhpt.digital.service;

public interface EmailService {
    void sendVerificationEmail(String to, String code);

    void sendResetPasswordEmail(String to, String code);

    void sendEmailWithAttachment(String to, String subject, String body,
            String attachmentName, byte[] attachmentData, String contentType);
}
