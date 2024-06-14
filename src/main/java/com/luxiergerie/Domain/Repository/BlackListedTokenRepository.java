package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.BlackListedToken;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;




/**
 * This interface represents a repository for managing blacklisted tokens.
 */
public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, UUID> {


  public BlackListedToken findByUserIdAndIsBlackListed(UUID userId, boolean isBlackListed);

  public BlackListedToken findByToken(String token);
}
