package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.entity.Proveedor;
import co.com.api.wise_stock.entity.ProveedorArticulo;
import co.com.api.wise_stock.service.ProveedorService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/proveedor")
@CrossOrigin
public class ProveedorController {
	
	@Autowired
	ProveedorService proveedorService;
	
	@GetMapping
	public Response lista() {
		return proveedorService.listaProveedores();
	}
	
	@PostMapping("/save")
	public Response saveProveedor(@RequestBody Proveedor proveedor) {
		return proveedorService.saveProveedor(proveedor);
	}
	
	@PostMapping("/articulo/save")
	public Response saveProveedor(@RequestBody ProveedorArticulo proveedorArticulo) {
		return proveedorService.saveProveedorArticulo(proveedorArticulo);
	}
	
	
	@PutMapping("/update")
	public Response updateProveedor(@RequestBody Proveedor proveedor) {
		return proveedorService.updateProveedor(proveedor);
	}

}
