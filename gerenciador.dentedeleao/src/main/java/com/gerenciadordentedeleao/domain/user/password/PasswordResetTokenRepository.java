package com.gerenciadordentedeleao.domain.user.password;

import com.gerenciadordentedeleao.domain.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByTokenAndUsed(String token, boolean used);

    void deleteByUser(UserEntity user);

    @Modifying
    @Query("UPDATE PasswordResetToken p SET p.used = true WHERE p.token = :token")
    void markTokenAsUsed(String token);
}
