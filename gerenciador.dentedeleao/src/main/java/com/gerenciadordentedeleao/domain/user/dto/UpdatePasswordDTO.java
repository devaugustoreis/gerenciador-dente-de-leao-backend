package com.gerenciadordentedeleao.domain.user.dto;

public record UpdatePasswordDTO(
        String oldPassword,
        String newPassword
) {
}
