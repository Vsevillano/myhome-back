package com.myhome.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myhome.models.RefreshToken;
import com.myhome.models.User;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
  Optional<RefreshToken> findByToken(String token);

  int deleteByUser(User user);
}