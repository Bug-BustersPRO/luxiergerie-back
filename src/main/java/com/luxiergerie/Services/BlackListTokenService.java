package com.luxiergerie.Services;

import com.luxiergerie.Domain.Entity.BlackListedToken;
import com.luxiergerie.Domain.Repository.BlackListedTokenRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    public boolean isBlacklistTokenExpired(Instant expiryDate, BlackListedToken blacklistedToken) {
      if (Instant.now().isAfter(expiryDate)) {
        blacklistedToken.setBlackListed(true);
      }
      return !blacklistedToken.isBlackListed();
    }

    public void deleteToken(String token) {
      BlackListedToken blacklistedToken = blacklistedTokenRepository.findByToken(token);
      blacklistedTokenRepository.delete(blacklistedToken);
    }

  @Scheduled(fixedRate = 600000)
  public void checkExpiredTokens() {
    List<BlackListedToken> tokens = blacklistedTokenRepository.findAll();
    Instant now = Instant.now();

    for (BlackListedToken token : tokens) {
      if (!token.isBlackListed() && now.isAfter(token.getExpiryDate())) {
        token.setBlackListed(true);
        blacklistedTokenRepository.save(token);
      }
    }
  }

  public void saveToken(String token, Instant expiryDate, UUID userId) {
    BlackListedToken blacklistedToken = new BlackListedToken();
    blacklistedToken.setToken(token);
    blacklistedToken.setExpiryDate(expiryDate);
    blacklistedToken.setUserId(userId);
    blacklistedToken.setBlackListed(false);
    blacklistedTokenRepository.save(blacklistedToken);
  }

}
