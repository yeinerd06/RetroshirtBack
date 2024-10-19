package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.entity.InformeCaja;
import co.com.api.wise_stock.service.InformeCajaService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/informe/caja")
@CrossOrigin

public class InformeCajaController {
	
	@Autowired
	InformeCajaService informeCajaService;
	
	@GetMapping
	public Response listado() {
		return informeCajaService.listaInforme();
	}
	
	@PostMapping("/save")
	public Response saveInforme(@RequestBody InformeCaja informeCaja) {
		return informeCajaService.saveInformeCaja(informeCaja);
	}
	
	@DeleteMapping("/delete/{id}")
	public Response deleteInforme(@PathVariable Integer id) {
		return informeCajaService.deleteInformeCaja(id);
	}
	
	

}
