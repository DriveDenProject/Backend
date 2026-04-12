package com.driveden.app.application.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${email.sender}")
    private String senderEmail;

    private final JavaMailSender mailSender;

    public void sendVerificationCode(String to, String code) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String html = generateHTML(code);

            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject("Código de verificación");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    public String generateHTML(String code){
        return """
            <!DOCTYPE html>
            <html>
            <body style="margin:0;padding:0;background-color:#f4f4f4;font-family:Arial,sans-serif;">
            <table width="100%%" cellpadding="0" cellspacing="0">
                <tr>
                <td align="center">
                    <table width="400" cellpadding="20" cellspacing="0" style="background:#ffffff;margin-top:40px;border-radius:10px;">
                    <tr>
                        <td align="center">
                        <h2 style="margin:0;color:#333;">Verificación de cuenta</h2>
                        <p style="color:#777;">Usa el siguiente código para continuar</p>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                        <div style="font-size:32px;font-weight:bold;letter-spacing:8px;color:#2d89ef;">
                            %s
                        </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                        <p style="color:#555;margin:0;">
                            Este código expira en <b>10 minutos</b>.
                        </p>
                        </td>
                    </tr>
                    <tr>
                        <td align="center">
                        <p style="color:#aaa;font-size:12px;">
                            Si no solicitaste este código, puedes ignorar este mensaje.
                        </p>
                        </td>
                    </tr>
                    </table>
                </td>
                </tr>
            </table>
            </body>
            </html>
            """.formatted(code);
    }
}
