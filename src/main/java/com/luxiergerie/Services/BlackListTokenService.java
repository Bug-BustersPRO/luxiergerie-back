package com.luxiergerie.Services;

import com.luxiergerie.Domain.Entity.BlackListedToken;
import com.luxiergerie.Domain.Repository.BlackListedTokenRepository;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class BlackListTokenService {
  private final BlackListedTokenRepository blacklistedTokenRepository;

    @Autowired
    public BlackListTokenService(BlackListedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    public void blacklistToken(String token, Date expiryDate, UUID userId) {
        BlackListedToken blacklistedToken = new BlackListedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiryDate(expiryDate);
        blacklistedToken.setUserId(userId);
        blacklistedTokenRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }
}
