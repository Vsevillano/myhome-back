package com.myhome.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myhome.models.ERole;
import com.myhome.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
	Optional<Role> findByName(ERole name);
}