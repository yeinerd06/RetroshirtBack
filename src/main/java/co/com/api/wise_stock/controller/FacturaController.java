package co.com.api.wise_stock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.api.wise_stock.entity.Devolucion;
import co.com.api.wise_stock.entity.Factura;
import co.com.api.wise_stock.service.FacturaService;
import co.com.api.wise_stock.util.Response;

@RestController
@RequestMapping("/factura")
@CrossOrigin
public class FacturaController {
	
	@Autowired
	FacturaService facturaService;
	
	
	@GetMapping
	public Response listadoFacturas() {
		return facturaService.listadoFacturas();
	}
	
	@GetMapping("/pago/{id}")
	public Response listadoFacturas(@PathVariable Integer id) {
		return facturaService.savePagoFactura(id);
	}
	
	@PostMapping("/save")
	public Response saveFactura(@RequestBody Factura factura) {
		return facturaService.saveFacturaVenta(factura);
	}
	@PutMapping("/update")
	public Response updateFactura(@RequestBody Factura factura) {
		return facturaService.updateFactura(factura);
	
	}
	@PostMapping("/devolucion/{id}/{nuevoTotal}")
	public Response devolcionFactura(@PathVariable Integer id, @RequestBody List<Devolucion> devoluciones,@PathVariable Integer nuevoTotal) {
		System.err.println(nuevoTotal);
		return facturaService.saveDevolucion(id, devoluciones,nuevoTotal);
	}

}
