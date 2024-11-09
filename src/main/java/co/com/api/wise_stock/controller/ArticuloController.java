package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.com.api.wise_stock.dto.UsuarioDto;
import co.com.api.wise_stock.entity.Articulo;
import co.com.api.wise_stock.service.ArticuloService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/articulo")
@CrossOrigin
public class ArticuloController {
	
	@Autowired
	ArticuloService articuloService;
	
	@GetMapping
	public Response listadoArticulos() {
		return articuloService.listadoArticulos();
	}
	
	@PreAuthorize("hasRole('ADMIN', 'ALMACENISTA')")
	@PutMapping("/update")
	public Response updateArticulo(@RequestPart("articulo") Articulo articulo, @RequestPart(value="file", required = false) MultipartFile file) {
		System.err.print(articulo.toString());
		return articuloService.updateArticulo(articulo, file);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'ALMACENISTA')")
	@PostMapping("/save")
	public Response saveArticulo(@RequestPart("articulo") Articulo articulo, @RequestPart("file") MultipartFile file) {
		
		return articuloService.saveArticulo(articulo, file);
	}
}
