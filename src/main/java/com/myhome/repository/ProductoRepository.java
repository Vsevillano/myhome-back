package com.myhome.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myhome.models.Producto;

public interface ProductoRepository extends MongoRepository<Producto, String> {
//	List<Producto> findByNombre(String nombre);
	Producto findByNombre(String nombre);
	
}
