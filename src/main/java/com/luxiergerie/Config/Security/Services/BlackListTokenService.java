package com.luxiergerie.Config.Security.Services;

import com.luxiergerie.Model.Entity.BlackListedToken;
import com.luxiergerie.Repository.BlackListedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class represents a service for managing blacklisted tokens.
 */
@Service
public class BlackListTokenService {
    private final BlackListedTokenRepository blacklistedTokenRepository;
    private final JwtDecoder jwtDecoder;

    @Autowired
    public BlackListTokenService(BlackListedTokenRepository blacklistedTokenRepository, JwtDecoder jwtDecoder) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
        this.jwtDecoder = jwtDecoder;
    }

    public boolean isBlacklistTokenExpired(Instant expiryDate, BlackListedToken blacklistedToken) {
        if (Instant.now().isAfter(expiryDate)) {
            blacklistedToken.setBlackListed(true);
        }
        return blacklistedToken.isBlackListed();
    }

    public void deleteToken(String token) {
        BlackListedToken blacklistedToken = blacklistedTokenRepository.findByToken(token);
        blacklistedTokenRepository.delete(blacklistedToken);
    }

    @Scheduled(cron = "0 0 11 * * *")
    public void checkExpiredTokens() {
        List<BlackListedToken> tokens = blacklistedTokenRepository.findAll();
        Instant now = Instant.now();

        for (BlackListedToken token : tokens) {
            try {
                Jwt jwt = this.jwtDecoder.decode(token.getToken());
                Map<String, Object> claims = jwt.getClaims();
                Instant expiryDate = (Instant) claims.get("exp");
                System.out.println(expiryDate);
                if (!token.isBlackListed() && (now.isAfter(token.getExpiryDate()) || now.isAfter(expiryDate))) {
                    token.setBlackListed(true);
                    blacklistedTokenRepository.save(token);
                }
            } catch (org.springframework.security.oauth2.jwt.JwtValidationException e) {
                if (e.getMessage().contains("Jwt expired")) {
                    token.setBlackListed(true);
                    blacklistedTokenRepository.save(token);
                }
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
