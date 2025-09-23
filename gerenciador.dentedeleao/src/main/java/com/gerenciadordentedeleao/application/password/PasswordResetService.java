package com.gerenciadordentedeleao.application.password;

import com.gerenciadordentedeleao.domain.user.UserEntity;
import com.gerenciadordentedeleao.domain.user.UserRepository;
import com.gerenciadordentedeleao.domain.user.password.PasswordResetToken;
import com.gerenciadordentedeleao.domain.user.password.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.password-reset.token.expiration}")
    private long tokenExpirationMs;

    /**
     * Cria uma solicitação de reset de senha para o usuário com o e-mail especificado
     *
     * @param email e-mail do usuário
     * @return true se a solicitação foi criada com sucesso, false caso contrário
     */
    @Transactional
    public boolean createPasswordResetRequest(String email) {
        Optional<UserEntity> userOptional = userRepository.findByLogin(email);

        if (userOptional.isEmpty()) {
            // Não informamos ao cliente se o e-mail existe ou não por segurança
            return false;
        }

        UserEntity user = userOptional.get();

        // Gera token único
        String token = generateToken();

        // Calcula data de expiração (padrão: 1 hora)
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(tokenExpirationMs / 1000);

        // Cria e salva o token
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, expiryDate);
        passwordResetTokenRepository.save(passwordResetToken);

        // Envia e-mail com o token (assíncrono)
        emailService.sendPasswordResetEmail(user.getLogin(), token, user.getFullName());

        return true;
    }

    /**
     * Valida um token de reset de senha
     *
     * @param token token a ser validado
     * @return true se o token é válido, false caso contrário
     */
    public boolean validateToken(String token) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);

        if (tokenOptional.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOptional.get();
        return resetToken.isValid();
    }

    /**
     * Redefine a senha do usuário com o token fornecido
     *
     * @param token token de reset de senha
     * @param newPassword nova senha
     * @return true se a senha foi redefinida com sucesso, false caso contrário
     */
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByTokenAndUsed(token, false);

        if (tokenOptional.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOptional.get();

        if (resetToken.isExpired()) {
            return false;
        }

        UserEntity user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Marca o token como usado
        passwordResetTokenRepository.markTokenAsUsed(token);

        return true;
    }

    /**
     * Gera um token único para reset de senha
     *
     * @return token único
     */
    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
