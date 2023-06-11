package com.myhome.payload.response;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String refreshToken;
	private String id;
	private String username;
	private String email;
	private String telefono;
	private boolean activo;
	
	private List<String> roles;

	public JwtResponse(String accessToken, String refreshToken, String id, String username, String email, String telefono, List<String> roles, boolean activo) {
		this.token = accessToken;
		this.setRefreshToken(refreshToken);
		this.id = id;
		this.username = username;
		this.email = email;
		this.telefono = telefono;
		this.roles = roles;
		this.activo = activo;
	}
	
	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public boolean getActivo() {
		return activo;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
