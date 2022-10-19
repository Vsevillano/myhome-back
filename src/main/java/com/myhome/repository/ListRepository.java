package com.myhome.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myhome.models.Lista;

public interface ListRepository extends MongoRepository<Lista, String> {	
	List<Lista> findByNombre(String nombre);
}