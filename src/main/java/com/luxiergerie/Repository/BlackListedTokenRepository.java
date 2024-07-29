package com.luxiergerie.Repository;

import com.luxiergerie.Model.Entity.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, UUID> {
    public BlackListedToken findByUserIdAndIsBlackListed(UUID userId, boolean isBlackListed);

    public BlackListedToken findByToken(String token);

}
