package com.myhome.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myhome.models.Tarea;

public interface TareaRepository extends MongoRepository<Tarea, String> {
	List<Tarea> findByNombre(String nombre);
}
