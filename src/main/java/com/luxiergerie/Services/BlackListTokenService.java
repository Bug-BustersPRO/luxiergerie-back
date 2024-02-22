package com.luxiergerie.Services;

import com.luxiergerie.Domain.Entity.BlackListedToken;
import com.luxiergerie.Domain.Repository.BlackListedTokenRepository;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





/**
 * This class represents a service for managing blacklisted tokens.
 */
@Service
public class BlackListTokenService {
  private final BlackListedTokenRepository blacklistedTokenRepository;

    @Autowired
    public BlackListTokenService(BlackListedTokenRepository blacklistedTokenRepository) {
      this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    /**
     * Blacklists a token with the specified expiry date and user ID.
     *
     * @param token The token to be blacklisted.
     * @param expiryDate The expiry date of the token.
     * @param userId The ID of the user associated with the token.
     */
    public void blacklistToken(String token, Date expiryDate, UUID userId) {
      BlackListedToken blacklistedToken = new BlackListedToken();
      blacklistedToken.setToken(token);
      blacklistedToken.setExpiryDate(expiryDate);
      blacklistedToken.setUserId(userId);
      blacklistedTokenRepository.save(blacklistedToken);
    }

    /**
     * Checks if a token is blacklisted.
     *
     * @param token The token to be checked.
     * @return true if the token is blacklisted, false otherwise.
     */
    public boolean isTokenBlacklisted(String token) {
      return blacklistedTokenRepository.existsByToken(token);
    }
}
