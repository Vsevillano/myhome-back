package com.myhome.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myhome.models.User;
import com.myhome.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@GetMapping("/users")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> users = new ArrayList<User>();

			userRepository.findAll().forEach(users::add);

			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Obtiene un usuario por ID
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUsuarioById(@PathVariable("id") String id) {
		Optional<User> usuarioData = userRepository.findById(id);

		if (usuarioData.isPresent()) {
			return new ResponseEntity<>(usuarioData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Crea un nuevo usuario
	 * 
	 * @param usuario
	 * @return
	 */
	@PostMapping("/createuser")
	public ResponseEntity<User> createUsuario(@RequestBody User usuario) {
		try {
			User _usuario = userRepository
					.save(new User(usuario.getNombre(), usuario.getApellidos(), usuario.getUsername(),
							usuario.getEmail(), usuario.getTelefono(), usuario.getPassword(), usuario.getActivo()));
			return new ResponseEntity<>(_usuario, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Edita un usuario
	 * 
	 * @param id
	 * @param usuario
	 * @return
	 */
	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUsuario(@PathVariable("id") String id, @RequestBody User usuario) {
		Optional<User> usuarioData = userRepository.findById(id);

		if (usuarioData.isPresent()) {
			User _usuario = usuarioData.get();
			_usuario.setNombre(usuario.getNombre());
			_usuario.setApellidos(usuario.getApellidos());
			_usuario.setTelefono(usuario.getApellidos());
			_usuario.setActivo(usuario.getActivo());
			return new ResponseEntity<>(userRepository.save(_usuario), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	/**
	 * Edita un usuario
	 * 
	 * @param id
	 * @param usuario
	 * @return
	 */

	@PutMapping("/users/active/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<User>> activateDesactivateUsuario(@PathVariable("id") String id, @RequestBody boolean estado) {
		Optional<User> usuarioData = userRepository.findById(id);

		try {
			if (usuarioData.isPresent()) {
				User _usuario = usuarioData.get();
				_usuario.setActivo(estado);	
				userRepository.save(_usuario);
			} 
			List<User> users = new ArrayList<User>();

			userRepository.findAll().forEach(users::add);

			if (users.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Elimina a un usuario por ID
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/users/{id}")
	public ResponseEntity<HttpStatus> deleteUsuario(@PathVariable("id") String id) {
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Elimina a todos los usuarios
	 * 
	 * @return
	 */
	@DeleteMapping("/users")
	public ResponseEntity<HttpStatus> deleteAllUsuarios() {
		try {
			userRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Obtiene los usuarios activos
	 * 
	 * @return
	 */
	@GetMapping("/usuarios/published")
	public ResponseEntity<List<User>> findByActivo() {
		try {
			List<User> usuarios = userRepository.findByActivo(true);

			if (usuarios.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(usuarios, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
