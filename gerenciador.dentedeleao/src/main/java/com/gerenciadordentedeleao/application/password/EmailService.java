package com.gerenciadordentedeleao.application.password;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    /**
     * Envia um e-mail de recuperação de senha de forma assíncrona
     *
     * @param to endereço de e-mail do destinatário
     * @param token token de recuperação de senha
     * @param userName nome do usuário
     */
    @Async
    public void sendPasswordResetEmail(String to, String token, String userName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Redefinição de Senha - Clínica Dente de Leão");

            String resetUrl = frontendUrl + "/reset-password?token=" + token;
            String htmlContent = createResetPasswordEmailContent(userName, resetUrl);

            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Log do erro, mas não lançamos exceção para não bloquear o fluxo principal
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }

    /**
     * Cria o conteúdo HTML do e-mail de recuperação de senha
     */
    private String createResetPasswordEmailContent(String userName, String resetUrl) {
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>");
        htmlContent.append("<h2 style='color: #4CAF50;'>Clínica Dente de Leão - Redefinição de Senha</h2>");
        htmlContent.append("<p>Olá, ").append(userName).append("!</p>");
        htmlContent.append("<p>Recebemos uma solicitação para redefinir sua senha.</p>");
        htmlContent.append("<p>Clique no botão abaixo para definir uma nova senha. Este link é válido por 1 hora.</p>");
        htmlContent.append("<a href='").append(resetUrl).append("' style='display: inline-block; background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; margin: 15px 0;'>Redefinir Senha</a>");
        htmlContent.append("<p>Se você não solicitou a redefinição de senha, por favor ignore este e-mail.</p>");
        htmlContent.append("<p>Atenciosamente,<br>Equipe Clínica Dente de Leão</p>");
        htmlContent.append("</div>");

        return htmlContent.toString();
    }
}
