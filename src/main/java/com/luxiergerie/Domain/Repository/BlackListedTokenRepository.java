package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.BlackListedToken;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;




/**
 * This interface represents a repository for managing blacklisted tokens.
 */
public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, Long> {

  // /**
  //  * Checks if a token exists in the blacklist.
  //  *
  //  * @param token the token to check
  //  * @return true if the token exists in the blacklist, false otherwise
  //  */
  // public boolean findByToken(String token);

  public BlackListedToken findByUserId(UUID userId);

  public BlackListedToken findByToken(String token);
}
