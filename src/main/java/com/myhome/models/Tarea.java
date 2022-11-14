package com.myhome.models;

import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tareas")
public class Tarea {
	@Id
	private String id;

	private String nombre;
	private String categoria;
	private String descripcion;
	private String fecha;
	private String estado;
	
	
	private String user;
		
	public Tarea() {		
	}

	public Tarea(String nombre, String categoria, String descripcion, String fecha, String estado, String user) {
		super();
		this.nombre = nombre;
		this.categoria = categoria;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.estado = estado;
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
		

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Tarea [id=" + id + ", nombre=" + nombre + ", categoria=" + categoria + ", descripcion=" + descripcion
				+ ", fecha=" + fecha + ", estado=" + estado + ", user=" + user + "]";
	}



}
