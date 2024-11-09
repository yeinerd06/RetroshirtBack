package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.service.RolServicie;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/rol")
@CrossOrigin
public class RolController {

	@Autowired
	RolServicie rolServicio;
	
	// @PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public Response listadoRol() {
		return rolServicio.listadoRoles();
	}
}
