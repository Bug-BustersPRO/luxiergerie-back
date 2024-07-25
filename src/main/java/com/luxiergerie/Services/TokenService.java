package com.luxiergerie.Services;

import com.luxiergerie.Domain.Entity.BlackListedToken;
import com.luxiergerie.Domain.Entity.Employee;
import com.luxiergerie.Domain.Entity.Sojourn;
import com.luxiergerie.Domain.Repository.BlackListedTokenRepository;
import com.luxiergerie.Domain.Repository.EmployeeRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.luxiergerie.Domain.Repository.SojournRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for generating JWT tokens for authentication.
 */
@Service
public class TokenService {

  private final JwtEncoder encoder;
  private final BlackListTokenService blackListTokenService;
  private final BlackListedTokenRepository blackListedTokenRepository;
  private final EmployeeRepository  employeeRepository;
  private final SojournRepository sojournRepository;
  private final JwtDecoder jwtDecoder;
  private boolean isEmployee = true;
  private Sojourn sojourn;

  /**
   * Constructs a TokenService object with the specified JwtEncoder.
   *
   * @param encoder        the JwtEncoder used to encode the JWT tokens.
   */
  public TokenService(JwtEncoder encoder, BlackListTokenService blackListTokenService,
                      BlackListedTokenRepository blackListedTokenRepository, EmployeeRepository employeeRepository, SojournRepository sojournRepository, JwtDecoder jwtDecoder) {
    this.encoder = encoder;
    this.blackListTokenService = blackListTokenService;
    this.blackListedTokenRepository = blackListedTokenRepository;
    this.employeeRepository = employeeRepository;
    this.sojournRepository = sojournRepository;
    this.jwtDecoder = jwtDecoder;
  }

  /**
   * Generates a JWT token based on the provided authentication information.
   * @param auth the authentication object containing the user's credentials and authorities.
   * @return the generated JWT token.
   */
  public String generateToken(Authentication auth) {
    UUID userId = findUserId(auth);

    BlackListedToken blackListedToken = blackListedTokenRepository.findByUserIdAndIsBlackListed(userId, false);
    if (blackListedToken != null) {
      if (blackListTokenService.isBlacklistTokenExpired(blackListedToken.getExpiryDate(), blackListedToken)) {
        String newToken = createAndEncodeJwt(auth);
        saveToken(newToken, userId);
      }
      return blackListedToken.getToken();
    }

    String jwtEncoded = createAndEncodeJwt(auth);
    saveToken(jwtEncoded, userId);
    return jwtEncoded;
  }

  private UUID findUserId(Authentication auth) {
    Employee employee = employeeRepository.findBySerialNumber(auth.getName());
    if (employee != null) {
      return employee.getId();
    } else {
      Sojourn sojourn = sojournRepository.findBySojournIdentifier(auth.getName());
      if (sojourn != null && sojourn.getClient() != null && sojourn.getPin() ==  Integer.parseInt(auth.getCredentials().toString())) {
        isEmployee = false;
        this.sojourn = sojourn;
        return sojourn.getClient().getId();
      } else {
        throw new RuntimeException("User not found with provided credentials");
      }
    }
  }

  private String createAndEncodeJwt(Authentication auth) {
    JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

    JwtClaimsSet claims = createJwtClaimsSet(auth, isEmployee, sojourn);

    return this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
  }

  private JwtClaimsSet createJwtClaimsSet(Authentication auth, boolean isEmployee, Sojourn sojourn) {
    Instant now = Instant.now();
    Instant expirationDate;

    if (isEmployee) {
      expirationDate = now.plus(24, ChronoUnit.HOURS);
    } else {
      LocalDateTime exitDateLocalDateTime = sojourn.getExitDate();
      Instant exitDate = exitDateLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
      if (now.isBefore(exitDate)) {
        long durationSeconds = ChronoUnit.SECONDS.between(now, exitDate);
        expirationDate = now.plusSeconds(durationSeconds);
      } else {
            throw new RuntimeException("Sojourn has already ended");
        }
    }

    String scope = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

    return JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(expirationDate)
            .subject(auth.getName())
            .claim("employeeSerialNumber", String.valueOf((auth.getPrincipal())))
            .claim("scope", scope)
            .build();
  }

  private void saveToken(String token, UUID userId) {
    if (isEmployee) {
      blackListTokenService.saveToken(token, Instant.now().plus(24, ChronoUnit.HOURS), userId);
    } else {
        blackListTokenService.saveToken(token, this.sojourn.getExitDate().atZone(ZoneId.systemDefault()).toInstant(), userId);
    }
  }

  private boolean validateToken(String token) {
    BlackListedToken blackListedToken = blackListedTokenRepository.findByToken(token);
    if (Objects.nonNull(blackListedToken) && !blackListedToken.isBlackListed()) {
      Date expirationDate = this.getTokenExpirationDate(String.valueOf(token));
      if (Instant.now().isAfter(expirationDate.toInstant()) || Instant.now().isAfter(blackListedToken.getExpiryDate())){
        blackListedToken.setBlackListed(true);
        blackListedTokenRepository.save(blackListedToken);
        return false;
      }
      return true;
    }
    return false;
  }

  public boolean isTokenValidAndNotExpired(String token) {
      return this.validateToken(token);
  }

  private Date getTokenExpirationDate(String token) {
    Jwt jwt = this.jwtDecoder.decode(token);
    Map<String, Object> claims = jwt.getClaims();
    Instant expInstant = (Instant) claims.get("exp");
    return new Date(expInstant.getEpochSecond() * 1000);
  }
}
