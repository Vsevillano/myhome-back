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

import com.myhome.models.Producto;
import com.myhome.models.Tarea;
import com.myhome.repository.ProductoRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProductoController {

  @Autowired
  ProductoRepository productoRepository;

  @GetMapping("/productos")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<List<Producto>> getAllProductos(@RequestParam(required = false) String nombre) {
    try {
      List<Producto> productos = new ArrayList<Producto>();
      
      productoRepository.findAll().forEach(productos::add);
      
      if (productos.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(productos, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/productos/{id}")
  public ResponseEntity<Producto> getProductoById(@PathVariable("id") String id) {
    Optional<Producto> productoData = productoRepository.findById(id);

    if (productoData.isPresent()) {
      return new ResponseEntity<>(productoData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/productos")
  public ResponseEntity<List<Producto>> createProducto(@RequestBody Producto producto) {
    try {
    	Producto _producto = productoRepository.save(new Producto(producto.getNombre()));
	    List<Producto> productos = new ArrayList<Producto>();
	    
	    for (Producto producto_a: productoRepository.findAll()) {       	        
	    		productos.add(producto_a);        	
	    }
	          	       
	    if (productos.isEmpty()) {
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	
	    return new ResponseEntity<>(productos, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/productos/{id}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<Producto> updateProducto(@PathVariable("id") String id, @RequestBody Producto producto) {
    Optional<Producto> productoData = productoRepository.findById(id);

    if (productoData.isPresent()) {
    Producto _producto = productoData.get();
    	_producto.setNombre(producto.getNombre());           
      return new ResponseEntity<>(productoRepository.save(_producto), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/productos/{id}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<HttpStatus> deleteProducto(@PathVariable("id") String id) {
    try {
    	productoRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/productos")
  public ResponseEntity<HttpStatus> deleteAllProductos() {
    try {
    	productoRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
