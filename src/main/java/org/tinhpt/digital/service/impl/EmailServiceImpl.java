package org.tinhpt.digital.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.tinhpt.digital.service.EmailService;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendVerificationEmail(String to, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject("Xác thực tài khoản Digital Bank");

            String content = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <h2 style="color: #2c3e50;">Xác thực tài khoản Digital Bank</h2>
                    <p>Chào bạn,</p>
                    <p>Cảm ơn bạn đã đăng ký tài khoản tại Digital Bank. Để hoàn tất quá trình đăng ký, vui lòng nhập mã xác thực sau:</p>
                    <div style="background-color: #f8f9fa; padding: 15px; margin: 20px 0; text-align: center;">
                        <h3 style="color: #2c3e50; margin: 0;">%s</h3>
                    </div>
                    <p>Mã xác thực này sẽ hết hạn sau 5 phút.</p>
                    <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.</p>
                    <p>Trân trọng,<br>Digital Bank Team</p>
                </div>
            """.formatted(code);

            helper.setText(content, true);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email xác thực", e);
        }
    }

    @Override
    public void sendResetPasswordEmail(String to, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject("Đặt lại mật khẩu Digital Bank");

            String content = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <h2 style="color: #2c3e50;">Đặt lại mật khẩu Digital Bank</h2>
                    <p>Chào bạn,</p>
                    <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. Vui lòng sử dụng mã xác thực sau:</p>
                    <div style="background-color: #f8f9fa; padding: 15px; margin: 20px 0; text-align: center;">
                        <h3 style="color: #2c3e50; margin: 0;">%s</h3>
                    </div>
                    <p>Mã xác thực này sẽ hết hạn sau 5 phút.</p>
                    <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này và đảm bảo tài khoản của bạn an toàn.</p>
                    <p>Trân trọng,<br>Digital Bank Team</p>
                </div>
            """.formatted(code);

            helper.setText(content, true);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email đặt lại mật khẩu", e);
        }
    }
}
