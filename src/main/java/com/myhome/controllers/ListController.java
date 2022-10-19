package com.myhome.controllers;

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

import com.myhome.models.Lista;
import com.myhome.repository.ListRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class ListController {

  @Autowired
  ListRepository listRepository;

  @GetMapping("/listas")
  public ResponseEntity<List<Lista>> getAllListas(@RequestParam(required = false) String nombre) {
    try {
      List<Lista> listas = new ArrayList<Lista>();

      if (nombre == null)
        listRepository.findAll().forEach(listas::add);
      else
    	  listRepository.findByNombre(nombre).forEach(listas::add);

      if (listas.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(listas, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/listas/{id}")
  public ResponseEntity<Lista> getLitaById(@PathVariable("id") String id) {
    Optional<Lista> listaData = listRepository.findById(id);

    if (listaData.isPresent()) {
      return new ResponseEntity<>(listaData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/listas")
  public ResponseEntity<Lista> createLista(@RequestBody Lista lista) {
    try {
    	Lista _lista = listRepository.save(new Lista(lista.getNombre()));
      return new ResponseEntity<>(_lista, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/listas/{id}")
  public ResponseEntity<Lista> updateLista(@PathVariable("id") String id, @RequestBody Lista lista) {
    Optional<Lista> listaData = listRepository.findById(id);

    if (listaData.isPresent()) {
      Lista _lista = listaData.get();
      _lista.setNombre(lista.getNombre());           
      return new ResponseEntity<>(listRepository.save(_lista), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/listas/{id}")
  public ResponseEntity<HttpStatus> deleteLista(@PathVariable("id") String id) {
    try {
      listRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/listas")
  public ResponseEntity<HttpStatus> deleteAllListas() {
    try {
      listRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
