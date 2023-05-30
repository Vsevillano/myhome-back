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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myhome.models.Tarea;
import com.myhome.repository.TareaRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TareaController {

  @Autowired
  TareaRepository tareaRepository;

  @GetMapping("/tareas")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<List<Tarea>> getAllTareas(@RequestParam(required = false) String nombre) {
    try {
      List<Tarea> tareas = new ArrayList<Tarea>();

      if (nombre == null)
    	  tareaRepository.findAll().forEach(tareas::add);
      else
    	  tareaRepository.findByNombre(nombre).forEach(tareas::add);

      if (tareas.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tareas, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/tareas/{id}")
  public ResponseEntity<Tarea> getTareaById(@PathVariable("id") String id) {
    Optional<Tarea> tareaData = tareaRepository.findById(id);

    if (tareaData.isPresent()) {
      return new ResponseEntity<>(tareaData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  
  @GetMapping("/tareas/user/{id}")
  public ResponseEntity<List<Tarea>> getTareaByUserId(@PathVariable("id") String id) {
  
    try {
        List<Tarea> tareas = new ArrayList<Tarea>();
        
        for (Tarea tarea: tareaRepository.findAll()) {
        	
        	if (tarea.getUser().toString().equals(id.toString())) {
        		tareas.add(tarea);
        	}
        }
              	       
        if (tareas.isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tareas, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @PostMapping("/tareas")  
  public ResponseEntity<List<Tarea>> createTarea(@RequestBody Tarea tarea) {	    
	try {
		tareaRepository.save(new Tarea(tarea.getNombre(), tarea.getCategoria(), tarea.getDescripcion(), tarea.getFecha(), tarea.getEstado(), tarea.getUser()));
	
	    List<Tarea> tareas = new ArrayList<Tarea>();
	    
	    for (Tarea tarea_a: tareaRepository.findAll()) {       	        
	    		tareas.add(tarea_a);        	
	    }
	          	       
	    if (tareas.isEmpty()) {
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	
	    return new ResponseEntity<>(tareas, HttpStatus.OK);
	  } catch (Exception e) {
	    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	  }
  }

  @PutMapping("/tareas/{id}")
  public ResponseEntity<List<Tarea>> updateTarea(@PathVariable("id") String id, @RequestBody Tarea tarea) {
    Optional<Tarea> tareaData = tareaRepository.findById(id);

    if (tareaData.isPresent()) {
    	Tarea _tarea = tareaData.get();
    	_tarea.setNombre(tarea.getNombre());
    	_tarea.setCategoria(tarea.getCategoria());
    	_tarea.setDescripcion(tarea.getDescripcion());
    	_tarea.setFecha(tarea.getFecha());
    	_tarea.setEstado(tarea.getEstado());
    	_tarea.setUser(tarea.getUser());
    	tareaRepository.save(_tarea);
        List<Tarea> tareas = new ArrayList<Tarea>();
        
        for (Tarea tarea_e: tareaRepository.findAll()) {
        	
        	if (tarea_e.getUser().toString().equals(id.toString())) {
        		tareas.add(tarea_e);
        	}
        }
              	       
        if (tareas.isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }    	
    	
        return new ResponseEntity<>(tareas, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/tareas/{id}")
  public ResponseEntity<List<Tarea>> deleteTarea(@PathVariable("id") String id) {

    try {
    	tareaRepository.deleteById(id);

        List<Tarea> tareas = new ArrayList<Tarea>();
        
        for (Tarea tarea_a: tareaRepository.findAll()) {        	        	
        		tareas.add(tarea_a);        	
        }
              	       
        if (tareas.isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tareas, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @DeleteMapping("/tareas")
  public ResponseEntity<HttpStatus> deleteAllTareas() {
    try {
    	tareaRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}
