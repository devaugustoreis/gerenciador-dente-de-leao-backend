package com.gerenciadordentedeleao.domain.user.dto;

public record UpdatePasswordDTO(
        String username,
        String oldPassword,
        String newPassword
) {
}
