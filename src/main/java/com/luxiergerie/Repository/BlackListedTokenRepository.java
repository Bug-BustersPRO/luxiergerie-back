package com.luxiergerie.Repository;

import com.luxiergerie.Model.Entity.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, UUID> {
    public BlackListedToken findByUserIdAndIsBlackListed(UUID userId, boolean isBlackListed);

    public BlackListedToken findByToken(String token);

}
