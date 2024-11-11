package co.com.api.wise_stock.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.dto.LoginDTO;
import co.com.api.wise_stock.dto.UsuarioDto;
import co.com.api.wise_stock.entity.Rol;
import co.com.api.wise_stock.entity.RolUsuario;
import co.com.api.wise_stock.entity.Usuario;
import co.com.api.wise_stock.repository.RolRepository;
import co.com.api.wise_stock.repository.RolUsuarioRepository;
import co.com.api.wise_stock.repository.UsuarioReporitory;
import co.com.api.wise_stock.security.JwtTokenProvider;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class LoginController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private UsuarioReporitory usuarioRepositorio;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	RolRepository rolRepository;
	@Autowired
	RolUsuarioRepository rolUsuarioRepository;

	@PostMapping("/login")
	public ResponseEntity<Response> authenticateUser(@RequestBody LoginDTO loginDTO) {
		Optional<Usuario> usuario = usuarioRepositorio.findByEmail(loginDTO.getEmail().toUpperCase());

		if (!usuario.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Response.crear(false, "Email no registrado", null));
		}
		
		if(!usuario.get().getEstado()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Response.crear(false, "USUARIO INACTIVO", null));
			
		}

		// Verificar si el usuario tiene acceso al módulo requerido
		/*
		boolean tieneAccesoAlModulo = false;
		for (Rol rol : usuario.get().getRoles()) {
			if (rol.getNombre().toLowerCase().equals("role_" + loginDTO.getModulo())) {
				tieneAccesoAlModulo = true;
				break;
			}
		}

		if (!tieneAccesoAlModulo) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(Response.crear(false, "El usuario no tiene acceso al módulo", null));
		}
		*/

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(usuario.get().getEmail(), loginDTO.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		// obtenemos el token del jwtTokenProvider
		String token = jwtTokenProvider.generarToken(authentication);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Expose-Headers", "Authorization, Bearer ");
		headers.add("Authorization", "Bearer " + token);
		return ResponseEntity.status(HttpStatus.OK).headers(headers)
				.body(Response.crear(true, "Usuario autenticado con exito", token));
	}
	
	
	 @PostMapping("/registrar")
	public Response registrarUsuario(@RequestBody UsuarioDto registroDTO) {
		
		 System.out.println(registroDTO.toString());
		Usuario usuario = new Usuario();
		usuario.setNombre(registroDTO.getNombre());
		usuario.setApellido(registroDTO.getApellido());
		usuario.setEmail(registroDTO.getEmail().toUpperCase());
		usuario.setEstado(true);
		usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
		usuario.setFecha(new Date());
		Optional<Rol> rol = rolRepository.findById(1);
		if (rol.isPresent()) {
			 System.out.println(rol.get().toString());
			RolUsuario rolUsuario = new RolUsuario();
			rolUsuario.setUsuario(usuarioRepositorio.save(usuario));
			rolUsuario.setRolId(rol.get().getId());
			rolUsuarioRepository.save(rolUsuario);
			return Response.crear(true, "Usuario registrado", null);

		}

		return Response.crear(false, "Error", null);

	}
	 
	 

	

}
