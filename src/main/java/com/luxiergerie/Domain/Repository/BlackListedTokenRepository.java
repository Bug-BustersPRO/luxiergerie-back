package com.luxiergerie.Domain.Repository;

import com.luxiergerie.Domain.Entity.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, Long>{
  boolean existsByToken(String token);
}
