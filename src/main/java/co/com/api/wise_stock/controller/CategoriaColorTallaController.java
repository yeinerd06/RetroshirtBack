package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.service.CategoriaColorTallaService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CategoriaColorTallaController {
	
	@Autowired
	CategoriaColorTallaService categoriaColorTallaService;
	
	@GetMapping("/categoria")
	public Response listaCategorias() {
		return categoriaColorTallaService.listaCategorias();
	}
	@GetMapping("/color")
	public Response listaColor() {
		return categoriaColorTallaService.listaColores();
	}
	@GetMapping("/talla")
	public Response listaTallas() {
		return categoriaColorTallaService.listaTallas();
	}
	
	

}
