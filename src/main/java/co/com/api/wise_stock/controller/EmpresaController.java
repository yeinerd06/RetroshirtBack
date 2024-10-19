package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.entity.Empresa;
import co.com.api.wise_stock.service.EmpresaService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/empresa")
@CrossOrigin
public class EmpresaController {
	
	@Autowired
	EmpresaService empresaService;
	
	@GetMapping
	public Response findEmpresa() {
		
		return empresaService.informacionEmpresa();
	}
	@PutMapping("/update")
	public Response updateEmpresa(@RequestBody Empresa empresa) {
		

		return empresaService.updateInformacionEmpresa(empresa);
	}

}
