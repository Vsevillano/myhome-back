package com.myhome.payload.request;

import java.util.Set;

import javax.validation.constraints.*;

public class ListaRequest {
	@NotBlank
	@Size(min = 3, max = 20)
	private String nombre;

	private Set<String> productos;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<String> getProductos() {
		return productos;
	}

	public void setProductos(Set<String> productos) {
		this.productos = productos;
	}


}
