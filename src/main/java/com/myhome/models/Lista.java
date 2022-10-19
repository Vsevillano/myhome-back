package com.myhome.models;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "listas")
public class Lista {
	@Id
	private String id;

	@NotBlank
	@Size(max = 20)
	private String nombre;

	@DBRef
	private Set<Producto> productos = new HashSet<>();

	public Lista() {

	}

	public Lista(String nombre) {
		this.nombre = nombre;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Producto> getProductos() {
		return productos;
	}

	public void setProductos(Set<Producto> productos) {
		this.productos = productos;
	}

	@Override
	public String toString() {
		return "Lista [id=" + id + ", nombre=" + nombre + ", productos=" + productos + "]";
	}

}
