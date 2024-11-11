package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.com.api.wise_stock.dto.UsuarioDto;
import co.com.api.wise_stock.entity.CodigoCambio;
import co.com.api.wise_stock.service.UsuarioService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/usuario")
@CrossOrigin
public class UsuarioController {

	@Autowired
	UsuarioService usuarioService;
	
	
	@GetMapping("/api")
	public Response api() {
		return Response.crear(true, "token valido", null);
	}

	// @PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public Response listadoUsuarios() {
		return usuarioService.listadoUsuarios();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/save")
	public Response saveProducto(@RequestPart("usuario") UsuarioDto usuario, @RequestPart("file") MultipartFile file) {
		
		// inyectorService.saveInyector(inyector)
		return usuarioService.saveUsuario(usuario, file);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update")
	public Response updateProducto(@RequestPart("usuario") UsuarioDto usuario,
			@RequestPart(value="file", required = false) MultipartFile file) {
		
		// inyectorService.saveInyector(inyector)
		return usuarioService.updateUsuario(usuario, file);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public Response eliminarUsuario(@PathVariable Integer id) {
		return usuarioService.deleteUsuario(id);
		
	}
	@PostMapping("/codigo-cambio")
	public Response sendEmailCambioPassword(@RequestBody CodigoCambio codigoRegistro) {

		return usuarioService.sendCodigoCambio(codigoRegistro);
	}

	@PostMapping("/cambio/{uuid}/{codigo}")
	public Response actualalizarPassword(@PathVariable String uuid, @PathVariable String codigo,
			@RequestBody CodigoCambio codigoCurren) {

		return usuarioService.validarCodigoCambioPassword(uuid, codigo, codigoCurren);
	}

	@PostMapping( value = "/cliente")
	public Response registrarCliente(
	@RequestPart("nombre") String nombre, 
    @RequestPart("apellido") String apellido, 
    @RequestPart("email") String email, 
    @RequestPart("password") String password, 
    @RequestPart("file") MultipartFile file) {
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setNombre(nombre);
		usuarioDto.setApellido(apellido);
		usuarioDto.setEmail(email);
		usuarioDto.setPassword(password);
		return usuarioService.registrarCliente(usuarioDto, file);
	}

}
