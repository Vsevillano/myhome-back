package com.myhome.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myhome.models.User;

public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	Optional<User> findById(Long userId);
	
	List<User> findByActivo(boolean activo);

	List<User> findByNombre(String nombre);
		
}