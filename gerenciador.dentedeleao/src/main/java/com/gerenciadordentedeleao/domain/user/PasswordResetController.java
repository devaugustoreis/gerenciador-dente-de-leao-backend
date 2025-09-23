package com.gerenciadordentedeleao.domain.user;

import com.gerenciadordentedeleao.application.password.PasswordResetService;
import com.gerenciadordentedeleao.domain.user.dto.PasswordResetDTO;
import com.gerenciadordentedeleao.domain.user.dto.PasswordResetRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private static final String MESSAGE = "message";

    private final PasswordResetService passwordResetService;

    /**
     * Endpoint para solicitar o reset de senha
     *
     * @param requestDTO contém o email do usuário
     * @return resposta HTTP 200 OK independente se o email existe ou não por segurança
     */
    @PostMapping("/reset-request")
    public ResponseEntity<Map<String, String>> requestPasswordReset(
            @RequestBody @Valid PasswordResetRequestDTO requestDTO) {

        // Por questões de segurança, sempre retornamos sucesso, mesmo se o email não existir
        passwordResetService.createPasswordResetRequest(requestDTO.getEmail());

        return ResponseEntity.ok(Map.of(
                MESSAGE, "Se o e-mail estiver cadastrado, você receberá instruções para redefinir sua senha."
        ));
    }

    /**
     * Endpoint para validar um token de reset de senha
     *
     * @param token token a ser validado
     * @return resposta com status do token
     */
    @GetMapping("/validate-token")
    public ResponseEntity<Map<String, Boolean>> validateToken(@RequestParam String token) {
        boolean isValid = passwordResetService.validateToken(token);

        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    /**
     * Endpoint para redefinir a senha utilizando o token
     *
     * @param resetDTO contém o token e a nova senha
     * @return resposta com o resultado da operação
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody @Valid PasswordResetDTO resetDTO) {
        boolean result = passwordResetService.resetPassword(resetDTO.getToken(), resetDTO.getPassword());

        if (result) {
            return ResponseEntity.ok(Map.of(
                    MESSAGE, "Senha redefinida com sucesso."
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    MESSAGE, "Token inválido ou expirado."
            ));
        }
    }
}
