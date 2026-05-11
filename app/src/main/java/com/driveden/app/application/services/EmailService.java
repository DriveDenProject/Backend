package com.driveden.app.application.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${brevo.key}")
    private String brevoApiKey;

    @Value("${email.sender}")
    private String senderEmail;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.brevo.com")
            .build();

    public void sendVerificationCode(String to, String code) {

        try {

            String html = generateHTML(code);

            Map<String, Object> body = Map.of(
                    "sender", Map.of(
                            "email", senderEmail
                    ),

                    "to", List.of(
                            Map.of(
                                    "email", to
                            )
                    ),

                    "subject", "Código de verificación",

                    "htmlContent", html
            );

            webClient.post()
                    .uri("/v3/smtp/email")
                    .header("api-key", brevoApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Correo enviado correctamente");

        } catch (Exception e) {

            System.out.println("Error enviando correo:");
            e.printStackTrace();

        }
    }

    public String generateCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    public String generateHTML(String code) {

        return """
            <!DOCTYPE html>
            <html>
            <body style="margin:0;padding:0;background-color:#f4f4f4;font-family:Arial,sans-serif;">
            <table width="100%%" cellpadding="0" cellspacing="0">
                <tr>
                <td align="center">
                    <table width="400" cellpadding="20" cellspacing="0"
                        style="background:#ffffff;margin-top:40px;border-radius:10px;">

                    <tr>
                        <td align="center">
                        <h2 style="margin:0;color:#333;">
                            Verificación de cuenta
                        </h2>

                        <p style="color:#777;">
                            Usa el siguiente código para continuar
                        </p>
                        </td>
                    </tr>

                    <tr>
                        <td align="center">
                        <div style="font-size:32px;font-weight:bold;
                                    letter-spacing:8px;color:#2d89ef;">
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
                            Si no solicitaste este código,
                            puedes ignorar este mensaje.
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