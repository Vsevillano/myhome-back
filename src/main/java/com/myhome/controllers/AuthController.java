package com.myhome.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myhome.excpetion.TokenRefreshException;
import com.myhome.models.ERole;
import com.myhome.models.EmailDetails;
import com.myhome.models.RefreshToken;
import com.myhome.models.Role;
import com.myhome.models.User;
import com.myhome.payload.request.ChangePasswordRequest;
import com.myhome.payload.request.LoginRequest;
import com.myhome.payload.request.SignupRequest;
import com.myhome.payload.request.TokenRefreshRequest;
import com.myhome.payload.response.JwtResponse;
import com.myhome.payload.response.MessageResponse;
import com.myhome.payload.response.TokenRefreshResponse;
import com.myhome.repository.RoleRepository;
import com.myhome.repository.UserRepository;
import com.myhome.security.jwt.JwtUtils;
import com.myhome.security.services.EmailService;
import com.myhome.security.services.RefreshTokenService;
import com.myhome.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RefreshTokenService refreshTokenService;
	
    @Autowired private EmailService emailService;


	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String jwt = jwtUtils.generateJwtToken(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

		return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
				userDetails.getUsername(), userDetails.getEmail(), userDetails.getTelefono(), roles, userDetails.getActivo()));
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		System.out.println(refreshTokenService.findByToken(requestRefreshToken));

		return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser).map(user -> {
					String token = jwtUtils.generateTokenFromUsername(user.getUsername());
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("El usuario ya existe"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Correo electrónico en uso"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getNombre(), signUpRequest.getApellidos(), signUpRequest.getUsername(),
				signUpRequest.getEmail(), signUpRequest.getTelefono(), encoder.encode(signUpRequest.getPassword()), true);

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role no encontrado."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role no encontrado."));
					roles.add(adminRole);
					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role no encontrado."));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role no encontrado."));
					roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);

		return new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
	}

	@PostMapping("/reset")
	public ResponseEntity<User> resetUserPassword(@RequestBody EmailDetails details) {
		try {
			if (userRepository.existsByEmail(details.getRecipient())) {
				Optional<User> user = userRepository.findByEmail(details.getRecipient());
				String token = jwtUtils.generateTokenFromUsername(user.get().getUsername());
				details.setMsgBody("Recuperar contraseña http://localhost:3000/newPassword/" + token);				
				emailService.sendSimpleMail(details);				
				return new ResponseEntity<>(null, HttpStatus.OK);
			}

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/changepassword")
	public ResponseEntity<User> changepassword(@RequestBody ChangePasswordRequest details) {
		try {
			
		    Optional<User> usuarioData = userRepository.findByUsername(details.getUsername());
		      if (usuarioData.isPresent()) {
		        User _usuario = usuarioData.get();		        		       
		        _usuario.setPassword(encoder.encode(details.getPassword()));
		        userRepository.save(_usuario);
		        return new ResponseEntity<>(null, HttpStatus.OK);
		      } else {
		        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		      }

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/checkResetPasswordToken/{token}")
	public ResponseEntity<?> checkResetPasswordToken(@PathVariable("token") String token) {
		try {			
			if (jwtUtils.validateJwtToken(token.toString())) {						
				return new ResponseEntity<>(null, HttpStatus.OK);
			}

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
