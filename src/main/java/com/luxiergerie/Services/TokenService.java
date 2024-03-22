package com.luxiergerie.Services;

import com.luxiergerie.Domain.Entity.BlackListedToken;
import com.luxiergerie.Domain.Entity.Employee;
import com.luxiergerie.Domain.Entity.Room;
import com.luxiergerie.Domain.Repository.BlackListedTokenRepository;
import com.luxiergerie.Domain.Repository.EmployeeRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.stream.Collectors;

import com.luxiergerie.Domain.Repository.RoomRepository;
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
  private final RoomRepository roomRepository;

  /**
   * Constructs a TokenService object with the specified JwtEncoder.
   *
   * @param encoder        the JwtEncoder used to encode the JWT tokens.
   * @param roomRepository
   */
  public TokenService(JwtEncoder encoder, BlackListTokenService blackListTokenService,
                      BlackListedTokenRepository blackListedTokenRepository, EmployeeRepository employeeRepository, RoomRepository roomRepository) {
    this.encoder = encoder;
    this.blackListTokenService = blackListTokenService;
    this.blackListedTokenRepository = blackListedTokenRepository;
    this.employeeRepository = employeeRepository;
    this.roomRepository = roomRepository;
  }

  /**
   * Generates a JWT token based on the provided authentication information.
   * @param auth the authentication object containing the user's credentials and authorities.
   * @return the generated JWT token.
   */
  public String generateToken(Authentication auth) {
    Employee employee = employeeRepository.findBySerialNumber(auth.getName());
    UUID userId;

    if (employee != null) {
      userId = employee.getId();
    } else {
      Room room = roomRepository.findByRoomNumber(Integer.parseInt(auth.getName()));
      if (room != null && room.getClient() != null && room.getClient().getPin() ==  Integer.parseInt(auth.getCredentials().toString())) {
        userId = room.getClient().getId();
      } else {
        throw new RuntimeException("User not found with provided credentials");
      }
    }

    BlackListedToken blackListedToken = blackListedTokenRepository.findByUserId(userId);
    if (blackListedToken != null) {
      if (blackListTokenService.isBlacklistTokenExpired(blackListedToken.getExpiryDate(), blackListedToken) == false) {
        return blackListedToken.getToken();
      } else {
        var newToken = "";
            // Generate the JwsHeader
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        // Get the current time
        Instant now = Instant.now();

        // Collect the authorities into a space-separated string
        String scope = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

        // Build the JwtClaimsSet
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(24*60, ChronoUnit.MINUTES))
            .subject(auth.getName())
            .claim("employeeSerialNumber", String.valueOf((auth.getPrincipal())))
            .claim("scope", scope)
            .build();

        var expirationDate = claims.getExpiresAt();
        // Encode the JWT token using the JwtEncoder
        newToken = this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        blackListedToken.setToken(newToken);
        blackListedToken.setExpiryDate(expirationDate);
        blackListedToken.setBlackListed(false);
        blackListedTokenRepository.save(blackListedToken);
        return blackListedToken.getToken();
      }
    }

    // Generate the JwsHeader
    JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

    // Get the current time
    Instant now = Instant.now();

    // Collect the authorities into a space-separated string
    String scope = auth.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));

    // Build the JwtClaimsSet
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(now)
        .expiresAt(now.plus(24*60, ChronoUnit.MINUTES))
        .subject(auth.getName())
        .claim("employeeSerialNumber", String.valueOf((auth.getPrincipal())))
        .claim("scope", scope)
        .build();

    var expirationDate = claims.getExpiresAt();
    // Encode the JWT token using the JwtEncoder
    var jwtEncoded = this.encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    blackListTokenService.blacklistToken(jwtEncoded, expirationDate, userId);
    return jwtEncoded;
  }
}