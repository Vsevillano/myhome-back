package com.myhome.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Usuario {
  @Id
  private String id;

  private String nombre;
  private String apellidos;
  private String email;
  private boolean activo;

  public Usuario() {

  }

  public Usuario(String nombre, String apellidos, String email, boolean activo) {
    this.nombre = nombre;
    this.apellidos = apellidos;
    this.email = email;
    this.activo = activo;
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

  public String getApellidos() {
    return apellidos;
  }

  public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
  }
  
  public String getEmail() {
	return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

  @Override
  public String toString() {
    return "Usuario [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + " + \", email=\" + email + \", activo=" + activo + "]";
  }
}
