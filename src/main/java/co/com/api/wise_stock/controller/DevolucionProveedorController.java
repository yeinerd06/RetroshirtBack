package co.com.api.wise_stock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.entity.ProveedorPedido;
import co.com.api.wise_stock.service.DevolucionProveedorService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/devolucion/proveedor")
public class DevolucionProveedorController {
	@Autowired
	DevolucionProveedorService devolucionProveedorService;
	
	
	@PutMapping("/confirmar")
	public Response confirmarDevolucion(@RequestBody ProveedorPedido proveedorPedido) {
		return devolucionProveedorService.confirmarDevolucion(proveedorPedido);
	}

}
