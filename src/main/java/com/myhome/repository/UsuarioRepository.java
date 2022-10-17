package com.myhome.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.myhome.models.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	List<Usuario> findByActivo(boolean activo);

	List<Usuario> findByNombre(String nombre);
}
