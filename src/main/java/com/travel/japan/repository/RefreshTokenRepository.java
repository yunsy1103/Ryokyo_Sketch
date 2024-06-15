package com.travel.japan.repository;

import com.travel.japan.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    //  Optional<RefreshToken> findByStudentId(String studentId);
    Optional<RefreshToken> findByRefreshToken(String token);
    Optional<RefreshToken> findByEmail(String email);
}
