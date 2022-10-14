package com.myhome.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myhome.model.Usuario;
import com.myhome.repository.UsuarioRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UsuarioController {

  @Autowired
  UsuarioRepository usuarioRepository;

  @GetMapping("/usuarios")
  public ResponseEntity<List<Usuario>> getAllUsuarios(@RequestParam(required = false) String nombre) {
    try {
      List<Usuario> usuarios = new ArrayList<Usuario>();

      if (nombre == null)
        usuarioRepository.findAll().forEach(usuarios::add);
      else
    	  usuarioRepository.findByNombre(nombre).forEach(usuarios::add);

      if (usuarios.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(usuarios, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/usuarios/{id}")
  public ResponseEntity<Usuario> getUsuarioById(@PathVariable("id") String id) {
    Optional<Usuario> usuarioData = usuarioRepository.findById(id);

    if (usuarioData.isPresent()) {
      return new ResponseEntity<>(usuarioData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/usuarios")
  public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
    try {
    	Usuario _usuario = usuarioRepository.save(new Usuario(usuario.getNombre(), usuario.getApellidos(), usuario.getEmail(), false));
      return new ResponseEntity<>(_usuario, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/usuarios/{id}")
  public ResponseEntity<Usuario> updateUsuario(@PathVariable("id") String id, @RequestBody Usuario usuario) {
    Optional<Usuario> usuarioData = usuarioRepository.findById(id);

    if (usuarioData.isPresent()) {
      Usuario _usuario = usuarioData.get();
      _usuario.setNombre(usuario.getNombre());
      _usuario.setApellidos(usuario.getApellidos());
      _usuario.setActivo(usuario.isActivo());
      return new ResponseEntity<>(usuarioRepository.save(_usuario), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/usuarios/{id}")
  public ResponseEntity<HttpStatus> deleteUsuario(@PathVariable("id") String id) {
    try {
      usuarioRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/usuarios")
  public ResponseEntity<HttpStatus> deleteAllUsuarios() {
    try {
      usuarioRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/usuarios/published")
  public ResponseEntity<List<Usuario>> findByActivo() {
    try {
      List<Usuario> usuarios = usuarioRepository.findByActivo(true);

      if (usuarios.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(usuarios, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
