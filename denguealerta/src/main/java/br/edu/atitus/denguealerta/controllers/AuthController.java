package br.edu.atitus.denguealerta.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.denguealerta.components.JwtUtils;
import br.edu.atitus.denguealerta.components.TipoUsuario;
import br.edu.atitus.denguealerta.dtos.SigninDTO;
import br.edu.atitus.denguealerta.dtos.SignupDTO;
import br.edu.atitus.denguealerta.entities.UsuarioEntity;
import br.edu.atitus.denguealerta.services.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UsuarioService usuarioService;
	
	private final AuthenticationConfiguration authConfig;

	public AuthController(UsuarioService usuarioService, AuthenticationConfiguration authConfig) {
		super();
		this.usuarioService = usuarioService;
		this.authConfig = authConfig;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<UsuarioEntity> signup(@RequestBody SignupDTO signup) throws Exception {
		UsuarioEntity novoUsuario = new UsuarioEntity();
		BeanUtils.copyProperties(signup, novoUsuario);
		novoUsuario.setTipo(TipoUsuario.Cidadao);
		this.usuarioService.save(novoUsuario);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<String> signin(@RequestBody SigninDTO signin) throws Exception {
		authConfig.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(signin.getEmail(), signin.getSenha()));
		String jwtToken = JwtUtils.generateTokenFromUsername(signin.getEmail());
		return ResponseEntity.ok(jwtToken);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exceptionHandler(Exception e) {
		String cleanMessage = e.getMessage().replaceAll("[\\r\\n]", " ");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cleanMessage);
	}
	
}
